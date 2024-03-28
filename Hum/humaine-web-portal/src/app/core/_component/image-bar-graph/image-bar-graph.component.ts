import { DurationType, errorHandler, formatNumber, getData, getKeyBasedData, successHandler } from './../../_utility/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ChartDataSets, ChartOptions, Chart, ChartType } from 'chart.js';
import { SingleDataSet, Label, monkeyPatchChartJsLegend, monkeyPatchChartJsTooltip, Color, Colors } from 'ng2-charts';
import { getLabels } from '../../_utility/common';
import { LiveService } from '../../../pages/live/live.service';
import { ToasterService } from '../../_utility/notify.service';
import { browsers, CONFIGCONSTANTS } from '../../_constant/app-constant';
import { browser } from 'protractor';
import * as _ from 'lodash';
@Component({
  selector: 'image-bar-graph',
  templateUrl: './image-bar-graph.component.html',
  styleUrls: ['./image-bar-graph.component.scss']
})

export class ImageBarGraphComponent implements OnInit {
  isPageLoading: boolean = false;
  public chartData: any[];
  interval: any;
  images = [
    '../../../../assets/svg/chrome-line.svg',
    '../../../../assets/svg/firefox-line.svg',
    '../../../../assets/svg/ie-line.svg',
    '../../../../assets/svg/opera-line.svg',
    '../../../../assets/svg/safari-line.svg'
  ];

  public lineChartData: ChartDataSets[] = [
  ];
  public lineChartLabels: Label[] = [
    browsers.CHROME,
    browsers.FIREFOX,
    browsers.IE,
    browsers.OPERA,
    browsers.SAFARI
  ];

  processBrowserData(data: any, label: string = '', defaultValue = 0) {
    if (!data) {
      this.lineChartData = [
        { data: [], label: label, borderWidth: 1.5 },
      ]
      return;
    }
    const result = [];
    this.lineChartLabels.forEach(e => {
      const filterObj = data.filter(b => b.browser == e);
      if (_.size(filterObj) > 0 && filterObj[0].count > 0) {
        result.push(filterObj[0].count);
      } else {
        result.push(defaultValue);
      }
    });
    this.lineChartData = [
      { data: result, label: label, borderWidth: 1.5 },
    ]
  }

  public gradientColors: Colors[] = [];
  public lineChartLegend = false;
  public lineChartType = 'bar';
  lineChartOptions: any = {
    responsive: true,
    animation: {
      duration: 0 // general animation time
    },
    hover: {
      animationDuration: 0 // duration of animations when hovering an item
    },
    responsiveAnimationDuration: 0,// animation duration after a resize

    tooltips: {
      enabled: false,
      // mode: 'index',
      // intersect: false,

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

          let innerHtml = '<thead style="background:white;border-radius:10px;"><span style="padding-left:10px;padding-right:10px;font-family:SofiaProBold, sans-serif !important;font-size:13px;">Active Devices Used</span>';

          titleLines.forEach(function (title) {
            innerHtml += '<tr><th style="background:white;border-radius:10px;padding-top:10px;padding-left:10px;padding-right:10px;color:#9999a9;"><span style="padding-left:5px;font-size: 11px;font-weight: normal;">' + title + '</span></th></tr>';
          });
          innerHtml += '</thead><tbody>';

          bodyLines.forEach(function (body, i) {
            let colors = tooltipModel.labelColors[i];
            let style = 'background:' + colors.backgroundColor;
            style += '; border-color:' + colors.borderColor;
            style += '; border-width: 2px';
            let span = '<span style="' + style + '"></span>';
            innerHtml += '<tr><td style="background:white;border-radius:10px;color:#70ece1;padding-left:10px;padding-right:10px;padding-bottom:5px;font-size:16px;">' + span + formatNumber(body, 2) + '</td></tr>';
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
        barThickness: 40,
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
          // beginAtZero: true,
          // steps : 10,
          display: true,
          zeroLineColor: "#f9f9fa",
          color: "#f9f9fa"
        },
        ticks: {
          fontColor: '#c8c8d1',
          callback: function (value, index, values) {
            return formatNumber(value, 2)
          }
        }
      }]
    }, plugins: {
      datalabels: {
        display: false,
      },
    }
  };

  lineChartPlugins = [
    {
      afterDraw: chart => {
        let ctx = chart.chart.ctx;
        let xAxis = chart.scales['x-axis-0'];
        let yAxis = chart.scales['y-axis-0'];
        let gradientStroke = ctx.createLinearGradient(0, 0, 0, 200);
        let dataset = chart.data.datasets[0];
        xAxis.ticks.forEach((value, index) => {

          let x = xAxis.getPixelForTick(index);
          let image = new Image();
          (image.src = this.images[index]),
            ctx.drawImage(image, x - 12, yAxis.bottom + 5, 20, 20);
        });
        gradientStroke.addColorStop(0, '#02decc');
        gradientStroke.addColorStop(1, '#60e7f2');
        this.gradientColors.push(gradientStroke);
        dataset.backgroundColor = gradientStroke;
        dataset.borderColor = gradientStroke;
        dataset.pointBorderColor = gradientStroke;
        dataset.hoverBorderColor = gradientStroke;
        dataset.hoverBackgroundColor = gradientStroke;
        
      }
    }
  ];

  constructor(private liveService: LiveService, private toaster: ToasterService) { }
  ngOnInit() {
    this.processBrowserData(null);
    this.getActiveDevice();
    this.getActiveDevice();
    // this.interval = setInterval(() => {
    // }, CONFIGCONSTANTS.chartDataInterval);
  }



  getActiveDevice() {
    this.isPageLoading = true;
    this.liveService.getActiveDevice().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.isPageLoading = false;
          this.processBrowserData(res);
        }
      );
    },
      (error) => {
        this.isPageLoading = false;
        errorHandler(this.toaster, error.error, () => { });
      }
    );
  }

  ngOnDestroy() {
    const tooltip = document.querySelector("#chartjs-tooltip")
    if (tooltip) { tooltip.remove() };
    clearInterval(this.interval);
  }
}
