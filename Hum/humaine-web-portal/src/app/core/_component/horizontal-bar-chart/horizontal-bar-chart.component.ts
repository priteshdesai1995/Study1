import { getTwentyFourHoursLables, getFortyEightHoursLables, getKeyBasedData, getLabels, formatNumber, DurationType, processData, processTimeData, secToTime, showLabelsInMin } from './../../_utility/common';
import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective, Label } from 'ng2-charts';
import { LiveService } from '../../../pages/live/live.service';
import {  CONFIGCONSTANTS, graphAPIparam } from '../../_constant/app-constant';
import { errorHandler, successHandler } from '../../_utility/common';
import { ToasterService } from '../../_utility/notify.service';
import DateUtils from '../../_utility/date';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
@Component({
  selector: 'horizontal-bar-chart',
  templateUrl: './horizontal-bar-chart.component.html',
  styleUrls: ['./horizontal-bar-chart.component.scss']
})
export class HorizontalBarChartComponent implements OnInit {
  @Input() dataType: string;
  durationTimeList = CONFIGCONSTANTS.durationTimeList;
  @ViewChild(BaseChartDirective) chart: BaseChartDirective;
  public lineChartType: ChartType = 'bar';
  apiParam = DurationType;
  public duration: DurationType = DurationType.TWENTY_FOUR_HOURS;
  isPageLoading = false;
  static date24Hours: string = '';
  static date48Hours: string = '';
  static timeUnit: string = '';
  static barData: any;
  public lineChartData: ChartDataSets[] = [];
  static currentTimeZonelables: {} = {};
  lineChartLabels: Label[] = [];
  interval:any;
  setDurationLables() {
    this.lineChartLabels = DateUtils.getLables(this.duration);
    HorizontalBarChartComponent.currentTimeZonelables = DateUtils.getCurrentTimeZoneLables(this.duration);
  }
  chartTypes = DurationType;
  get getChartType() {
    if (this.duration === DurationType.ONE_WEEK) {
      return 'horizontalBar';
    } else {
      return 'bar';
    }
  }
  setData(res) {
    this.lineChartData = processTimeData(res, this.duration, '', 0);    
  }
  horizontalbarlineChartOptions: ChartOptions = {
    animation: {
      duration: 0 // general animation time
    },
    hover: {
      animationDuration: 0 // duration of animations when hovering an item
    },
    responsiveAnimationDuration: 0,// animation duration after a resize
    responsive: true,
    tooltips: {
      enabled: false,
      mode: 'index',
      intersect: false,

      custom: function (tooltipModel) {
        // Tooltip Element
        let tooltipEl = document.getElementById('chartjs-tooltip');
        // Create element on first render
        if (!tooltipEl) {
          tooltipEl = document.createElement('div');
          tooltipEl.id = 'chartjs-tooltip';
          tooltipEl.innerHTML = '<table style="background:white;left:-50px;position:relative;padding:10px;border-radius:10px;box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px; "></table>';
          document.body.appendChild(tooltipEl);
        }

        // Hide if no tooltip
        if (tooltipModel.opacity === 0) {
          tooltipEl.style.opacity = "0";
          return;
        }

        // Set caret Position
        tooltipEl.classList.remove('above', 'below', 'no-transform');
        if (tooltipModel.yAlign) {
          tooltipEl.classList.add(tooltipModel.yAlign);
        } else {
          tooltipEl.classList.add('no-transform');
        }

        function getBody(bodyItem) {
          return bodyItem.lines;
        }

        // Set Text
        if (tooltipModel.body) {
          let titleLines = tooltipModel.title || [];
          let bodyLines = tooltipModel.body.map(getBody);
          let innerHtml = '<thead style="background:white;border-radius:10px;"><span style="padding-left:10px;padding-right:10px;font-family:SofiaProBold, sans-serif !important;font-size:13px;">Session Duration</span>';
          titleLines.forEach(function (title) {
            innerHtml += '<tr><th style="background:white;border-radius:10px;padding-top:10px;padding-left:10px;padding-right:10px;color:#9999a9;"><img  style="height:12px;" src="/assets/img/calendar-line.svg"><span style="padding-left:5px;font-size: 11px;font-weight: normal;">' + ((HorizontalBarChartComponent.currentTimeZonelables[tooltipModel.dataPoints[0].yLabel].long)) + '</span></th></tr>';
          });
          innerHtml += '</thead><tbody>';

          bodyLines.forEach(function (body, i) {
            let colors = tooltipModel.labelColors[i];
            let style = 'background:' + colors.backgroundColor;
            style += '; border-color:' + colors.borderColor;
            style += '; border-width: 2px';
            let span = '<span style="' + style + '"></span>';
            innerHtml += '<tr><td style="background:white;border-radius:10px;color:#70ece1;padding-left:10px;padding-right:10px;padding-bottom:5px;font-size:16px;">' + span + secToTime(body) + '</td></tr>';
          });
          innerHtml += '</tbody>';

          let tableRoot = tooltipEl.querySelector('table');
          tableRoot.innerHTML = innerHtml;
        }

        // `this` will be the overall tooltip
        let position = this._chart.canvas.getBoundingClientRect();

        // Display, position, and set styles for font
        tooltipEl.style.opacity = "1";
        tooltipEl.style.position = 'absolute';
        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
        tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
        tooltipEl.style.fontFamily = '"SofiaProMedium", sans-serif !important';
        tooltipEl.style.fontSize = tooltipModel.bodyFontSize + 'px';
        tooltipEl.style.fontStyle = tooltipModel._bodyFontStyle;
        tooltipEl.style.padding = tooltipModel.yPadding + 'px ' + tooltipModel.xPadding + 'px';
        // tooltipEl.style.pointerEvents = 'none';
        tooltipEl.style.transition = '0.23s';
      }
    },
    scales: {
      xAxes: [{
        gridLines: {
          display: true,
          zeroLineColor: "white",
          color: "white"
        },
        ticks: {
          fontColor: "white"
        }
      }],
      yAxes: [{
        gridLines: {
          display: true,
          zeroLineColor: "white",
          color: "white"
        },
        ticks: {
          display: true,
          callback: function(value, index, values) {
            return HorizontalBarChartComponent.currentTimeZonelables[value].day;
            // const arr = HorizontalBarChartComponent.currentTimeZonelables[value].short.split("T");
            // return arr[1];
          }
        }
      }]
    }, plugins: {
      datalabels: {
        display: true,
        anchor: 'end',
        align: 'end',
        color: "#39476c",
        font: {
          size: 11,
          weight: 600
        },
        formatter: (value, context) => {
          return secToTime(value);
        },
      },
    }
  };
  lineChartOptions: ChartOptions = {
    animation: {
      duration: 0 // general animation time
    },
    hover: {
      animationDuration: 0 // duration of animations when hovering an item
    },
    responsiveAnimationDuration: 0,// animation duration after a resize
    responsive: true,
    tooltips: {
      enabled: false,
      mode: 'index',
      intersect: false,

      custom: function (tooltipModel) {
        // Tooltip Element
        let tooltipEl = document.getElementById('chartjs-tooltip');
        // Create element on first render
        if (!tooltipEl) {
          tooltipEl = document.createElement('div');
          tooltipEl.id = 'chartjs-tooltip';
          tooltipEl.innerHTML = '<table style="background:white;left:-50px;position:relative;padding:10px;border-radius:10px;box-shadow: rgba(0, 0, 0, 0.1) 0px 4px 12px; "></table>';
          document.body.appendChild(tooltipEl);
        }

        // Hide if no tooltip
        if (tooltipModel.opacity === 0) {
          tooltipEl.style.opacity = "0";
          return;
        }

        // Set caret Position
        tooltipEl.classList.remove('above', 'below', 'no-transform');
        if (tooltipModel.yAlign) {
          tooltipEl.classList.add(tooltipModel.yAlign);
        } else {
          tooltipEl.classList.add('no-transform');
        }

        function getBody(bodyItem) {
          return bodyItem.lines;
        }

        // Set Text
        if (tooltipModel.body) {
          let titleLines = tooltipModel.title || [];
          let bodyLines = tooltipModel.body.map(getBody);

          let innerHtml = '<thead style="background:white;border-radius:10px;"><span style="padding-left:10px;padding-right:10px;font-family:SofiaProBold, sans-serif !important;font-size:13px;">Session Duration</span>';

          titleLines.forEach(function (title) {
            innerHtml += '<tr><th style="background:white;border-radius:10px;padding-top:10px;padding-left:10px;padding-right:10px;color:#9999a9;"><img  style="height:12px;" src="/assets/img/calendar-line.svg"><span style="padding-left:5px;font-size: 11px;font-weight: normal;">' + title + '</span></th></tr>';
          });
          innerHtml += '</thead><tbody>';

          bodyLines.forEach(function (body, i) {
            let colors = tooltipModel.labelColors[i];
            let style = 'background:' + colors.backgroundColor;
            style += '; border-color:' + colors.borderColor;
            style += '; border-width: 2px';
            let span = '<span style="' + style + '"></span>';
            // + span + formatNumber(body,2)+' Sec.'
            innerHtml += '<tr><td style="background:white;border-radius:10px;color:#70ece1;padding-left:10px;padding-right:10px;padding-bottom:5px;font-size:16px;">'  + ( secToTime(body) ? ''+secToTime(body)+'': '' ) + '</td></tr>';
          });
          innerHtml += '</tbody>';

          let tableRoot = tooltipEl.querySelector('table');
          tableRoot.innerHTML = innerHtml;
        }

        // `this` will be the overall tooltip
        let position = this._chart.canvas.getBoundingClientRect();

        // Display, position, and set styles for font
        tooltipEl.style.opacity = "1";
        tooltipEl.style.position = 'absolute';
        tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
        tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
        tooltipEl.style.fontFamily = '"SofiaProMedium", sans-serif !important';
        tooltipEl.style.fontSize = tooltipModel.bodyFontSize + 'px';
        tooltipEl.style.fontStyle = tooltipModel._bodyFontStyle;
        tooltipEl.style.padding = tooltipModel.yPadding + 'px ' + tooltipModel.xPadding + 'px';
        // tooltipEl.style.pointerEvents = 'none';
        tooltipEl.style.transition = '0.23s';
      }
    },
    scales: {
      xAxes: [{
        gridLines: {
          display: true,
          zeroLineColor: "white",
          color: "white"
        },
        ticks:{
          fontColor: '#c8c8d1',
          callback: function(value, index, values) {
            const arr = HorizontalBarChartComponent.currentTimeZonelables[value].short.split("T");
            return arr[1];
          }
        }
      }],
      yAxes: [{
        gridLines: {
          display: true,
          zeroLineColor: "white",
          color: "white"
        },
        ticks: {
          fontColor: '#c8c8d1',
          display: true,
          callback: function(value, index, values) {
            const num = Number(value);
            return num!=NaN ? showLabelsInMin(value) : value;
          }          
        }
      }]
    }, plugins: {
      datalabels: {
        display: false,
        // anchor: 'end',
        // align: 'end',
        // color: "#39476c",
        // font: {
        //   size: 11,
        //   weight: 600
        // },
        // formatter: (value, context) => {
        //   return `${value}min`;
        // },
      },
    }
  };
  lineChartPlugins = [{
    afterLayout: chart => {
      const ctx = chart.chart.ctx;
      const xAxis = chart.scales['x-axis-0'];
      const gradientStroke = ctx.createLinearGradient(0, 0, 0, 200);
      const dataset = chart.data.datasets[0];
      gradientStroke.addColorStop(0, '#02decc');
      gradientStroke.addColorStop(1, '#995af9');
      dataset.backgroundColor = gradientStroke;
      dataset.borderColor = gradientStroke;
      dataset.pointBorderColor = gradientStroke;
      dataset.hoverBorderColor = gradientStroke;
      dataset.hoverBackgroundColor = gradientStroke;
    }
  },
];
  horizontalineChartPlugin = [{
    afterLayout: chart => {
      const ctx = chart.chart.ctx;
      const xAxis = chart.scales['x-axis-0'];
      const gradientStroke = ctx.createLinearGradient(0, 0, 0, 200);
      const dataset = chart.data.datasets[0];
      gradientStroke.addColorStop(0, '#02decc');
      gradientStroke.addColorStop(1, '#995af9');
      dataset.backgroundColor = gradientStroke;
      dataset.borderColor = gradientStroke;
      dataset.pointBorderColor = gradientStroke;
      dataset.hoverBorderColor = gradientStroke;
      dataset.hoverBackgroundColor = gradientStroke;
    }
  },
  pluginDataLabels];
  lineChartLegend = false;

  constructor(private liveService: LiveService,
    private toaster: ToasterService) {

  }

  ngOnInit() {
    this.setDurationLables();
    this.setData(null);
    this.getSessionDurationData();
    this.getSessionDurationData();
    // this.interval = setInterval(() => {
    // }, CONFIGCONSTANTS.chartDataInterval);
  }
  getSessionDurationData() {
    this.setDurationLables();
    // this.setData(null);
    const param = "?duration=" + this.duration;
    this.liveService.getSessionDuration(param).subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          HorizontalBarChartComponent.date48Hours = res.fourtyEightHoursDate;
          HorizontalBarChartComponent.date24Hours = res.twentyFourHoursDate;
          HorizontalBarChartComponent.timeUnit = res.timeUnit;
          this.setData(res);
        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  durationChange(e) {
    this.lineChartType = "bar";
    this.lineChartOptions.plugins.datalabels.display = false;
    // this.setDatalabels();
    this.setDurationLables();
    this.setData(null);
    this.getSessionDurationData();
    // this.refreshChart();
  }

  setDatalabels() {
    if (this.duration === DurationType.ONE_WEEK) {
      this.lineChartType = "horizontalBar";
      this.lineChartOptions.plugins.datalabels = {
        display: false,
        anchor: 'end',
        align: 'end',
        color: "#39476c",
        font: {
          size: 11,
          weight: 600
        },
        formatter: (value, context) => {
          return `${value}min`;
        },

      }
    } else {
      
    }
  }
  refreshChart() {
    setTimeout(() => {
      if (this.chart && this.chart.chart && this.chart.chart.config) {
        this.chart.chart.config.data.labels = this.lineChartLabels;
        this.chart.chart.config.data.datasets = this.lineChartData;
        this.chart.chart.update();
      }
    });
  }
  ngOnDestroy() {
    const tooltip = document.querySelector("#chartjs-tooltip")
    if (tooltip) {tooltip.remove()};
    clearInterval(this.interval);
  }
}
