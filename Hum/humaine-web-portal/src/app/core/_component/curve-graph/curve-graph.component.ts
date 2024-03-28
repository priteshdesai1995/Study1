import { getTwentyFourHoursLables, getFortyEightHoursLables, getKeyBasedData, processFortyEightHoursData, formatNumber, DurationType, processTimeData, secToTime } from './../../_utility/common';
import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import * as Chart from 'chart.js';
import { SingleDataSet, Label, monkeyPatchChartJsLegend, monkeyPatchChartJsTooltip } from 'ng2-charts';
import { LiveService } from '../../../pages/live/live.service';
import { CONFIGCONSTANTS, graphAPIparam } from '../../_constant/app-constant';
import { errorHandler, successHandler } from '../../_utility/common';
import { ToasterService } from '../../_utility/notify.service';
import DateUtils from '../../_utility/date';

@Component({
  selector: 'curve-graph',
  templateUrl: './curve-graph.component.html',
  styleUrls: ['./curve-graph.component.scss']
})
export class CurveGraphComponent implements OnInit {
  @ViewChild('myCanvas')
  public canvas: ElementRef;
  public context: CanvasRenderingContext2D;
  static currentTimeZonelables: {} = {};
  public chartType: string = 'line';
  public chartData: any[];
  public chartLabels: any[];
  public chartColors: any[];
  public chartOptions: any;
  chart: Chart;
  static date24Hours:string = '';
  static date48Hours:string = '';
  static timeUnit:string = '';
  pageLoadData: any;
  interval: any;
  public duration: DurationType = DurationType.TWENTY_FOUR_HOURS;
  pageLoadSelection = CONFIGCONSTANTS.pageLoadSelection;
  apiParam = graphAPIparam;
  isPageLoading: boolean = false;

  constructor(private liveService: LiveService,
    private toaster: ToasterService) {
    monkeyPatchChartJsTooltip();
    monkeyPatchChartJsLegend();
  }

  setDurationLables() {
    this.chartLabels = DateUtils.getLables(this.duration)
    CurveGraphComponent.currentTimeZonelables = DateUtils.getCurrentTimeZoneLables(this.duration);
  }

  setData(res) {
    this.chartData = processTimeData(res, this.duration, '', 0, {
      tension: 0.1,
      borderRadius: 5
    });
  }

  ngOnInit() {
    this.setDurationLables();
    this.setData(null);
    this.chartColors = [{
      backgroundColor: 'rgb(0 0 0 / 0%)',
      borderColor: '#70ece1',
      borderWidth	: 2,
      pointBackgroundColor: 'white',
      pointBorderColor: '#70ece1',
      pointHoverBackgroundColor: 'white',
      pointHoverBorderColor: '#70ece1'    

    }];
    this.chartOptions = {
      animation: {
        duration: 0 // general animation time
      },
      hover: {
        animationDuration: 0 // duration of animations when hovering an item
      },
      responsiveAnimationDuration: 0,// animation duration after a resize
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

            let innerHtml = '<thead style="background:white;border-radius:10px;"><span style="padding-left:10px;padding-right:10px;font-family:SofiaProBold, sans-serif !important;font-size:13px;">Page Load Time</span>';

            titleLines.forEach(function (title) {
              innerHtml += '<tr><th style="background:white;border-radius:10px;padding-top:10px;padding-left:10px;padding-right:10px;color:#9999a9;"><img style="height:12px" src="../../../../assets/img/calendar-line.svg"><span style="padding-left:5px;font-size: 11px;font-weight: normal;">' +  ( ((CurveGraphComponent.currentTimeZonelables[tooltipModel.dataPoints[0].xLabel].long)) )  + '</span></th></tr>';
            });
            innerHtml += '</thead><tbody>';

            bodyLines.forEach(function (body, i) {
              let colors = tooltipModel.labelColors[i];
              let style = 'background:' + colors.backgroundColor;
              style += '; border-color:' + colors.borderColor;
              style += '; border-width: 2px';
              let span = '<span style="' + style + '"></span>';
              innerHtml += '<tr><td style="background:white;border-radius:10px;color:#70ece1;padding-left:10px;padding-right:10px;padding-bottom:5px;font-size:16px;">' + span + secToTime(formatNumber(body,2)) + '</td></tr>';
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
    
      bezierCurve: false,
      responsive: true,
      legend: {
        display: false
      },
      scales: {
        yAxes: [{
          gridLines: {
            display: true,
            zeroLineColor: "#f3f3f5",
            color: "#f3f3f5"
          },
          ticks: {
            fontColor: '#c8c8d1',
            fontSize: 12,
            callback: function(value, index, values) {
              return formatNumber(value, 2)
            }
          }
        }],
        xAxes: [{
          gridLines: {
            display: true,
            zeroLineColor: "#f3f3f5",
            color: "#f3f3f5",
          },
          ticks: {
            fontColor: '#c8c8d1',
            fontSize: 12,
            callback: function(value, index, values) {
              const arr = CurveGraphComponent.currentTimeZonelables[value].short.split("T");
              return arr[1];
            }
          }
        }]
      },
      plugins: {
        datalabels: {
          display: false,
        },
      }
    }
    this.getPageLoadData()
    this.getPageLoadData()
    // this.interval = setInterval(() => {
    // }, CONFIGCONSTANTS.chartDataInterval);

  };

  lineChartPlugins = [
    {
      afterDraw: chart => {
        if (chart.tooltip._active && chart.tooltip._active.length) {
          let activePoint = chart.tooltip._active[0],
            ctx = chart.ctx,
            x = activePoint.tooltipPosition().x,
            y = activePoint.tooltipPosition().y,
            topY = activePoint.tooltipPosition().y,
            bottomY = chart.scales['y-axis-0'].bottom;
          let gradientStroke = ctx.createLinearGradient(0, 0, 0, 600);
          gradientStroke.addColorStop(0, '#ffffff');
          gradientStroke.addColorStop(1, '#e1e1e6');
          // draw line
          ctx.save();
          ctx.beginPath();
          ctx.moveTo(x, topY);
          ctx.lineTo(x, bottomY);
          ctx.lineWidth = 8;
          ctx.strokeStyle = gradientStroke;
          ctx.stroke();
          ctx.restore();

          // draw Circle
          ctx.save();
          ctx.beginPath();
          ctx.arc(x, topY + 2, 5.5, 0, 2 * Math.PI);
          ctx.fillStyle = "white";
          ctx.fill();
          ctx.stroke();

        }
      }
    }
  ];

  onTabChange() {
    this.setDurationLables();
    this.setData(null);
    this.getPageLoadData();
  }
  getPageLoadData() {
    const param = "?duration=" + this.duration;
    this.liveService.getPageLoadTime(param).subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          CurveGraphComponent.date48Hours = res.fourtyEightHoursDate;
          CurveGraphComponent.date24Hours = res.twentyFourHoursDate;
          CurveGraphComponent.timeUnit = res.timeUnit;
          this.setData(res.data);
        }
      );
    },
      (error) => {
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  pageLoadChange(e) {
    this.getPageLoadData();
  }
  ngOnDestroy() {
    const tooltip = document.querySelector("#chartjs-tooltip")
    if (tooltip) {tooltip.remove()};
    clearInterval(this.interval);
  }
}


