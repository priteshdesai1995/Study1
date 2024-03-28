import { processTimeData, processData } from './../../_utility/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ChartDataSets } from 'chart.js';
import { LiveService } from '../../../pages/live/live.service';
import { CONFIGCONSTANTS } from '../../_constant/app-constant';
import { DurationType, errorHandler, formatNumber, getData, getLabels, successHandler } from '../../_utility/common';
import DateUtils from '../../_utility/date';
import { ToasterService } from '../../_utility/notify.service';

@Component({
  selector: 'thin-bar-graph',
  templateUrl: './thin-bar-graph.component.html',
  styleUrls: ['./thin-bar-graph.component.scss']
})
export class ThinBarGraphComponent implements OnInit {
  @ViewChild('myCanvas')
  public canvas: ElementRef;
  public context: CanvasRenderingContext2D;
  public chartType: string = 'bar';
  public chartData: any[];
  public chartLabels: any[];
  public chartColors: any[];
  public chartOptions: any;
  static currentTimeZonelables: {} = {};
  isPageLoading: boolean = false;
  interval: any;
  constructor(private liveService: LiveService, private toaster: ToasterService) { }

  ngOnInit() {
    this.chartLabels = DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS);
    ThinBarGraphComponent.currentTimeZonelables = DateUtils.getCurrentTimeZoneLables(DurationType.TWENTY_FOUR_HOURS);
    this.chartData = processData(null,DurationType.TWENTY_FOUR_HOURS);
    this.chartColors = [{
      backgroundColor: '#8a4aef',
      borderColor: '#a05afa'
    }];
    this.chartOptions = {
      animation: {
        duration: 0 // general animation time
      },
      hover: {
        animationDuration: 0 // duration of animations when hovering an item
      },
      responsiveAnimationDuration: 0,// animation duration after a resize
      responsive: true,
      legend: {
        display: false
      },
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
            tooltipEl.innerHTML = '<table style="width:100px;padding:10px;border-radius:5px;background: white;box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.2), 0 2px 10px 0 rgba(0, 0, 0, 0.19); "></table>';
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

            let innerHtml = '<thead style="background:white;border-radius:5px;"><span style="padding-left:10px;padding-right:10px;font-family:SofiaProBold, sans-serif !important;font-size:13px;">Bounce Rate</span>';

            titleLines.forEach(function (title) {
              innerHtml += '<tr><th style="background:white;border-radius:5px;padding-left:10px;padding-right:10px;color:#9999a9;"><img  style="height:12px;" src="/assets/img/calendar-line.svg"><span style="padding-left:5px;font-size: 11px;font-weight: normal;">' 
              + ((ThinBarGraphComponent.currentTimeZonelables[tooltipModel.dataPoints[0].xLabel].long)) +
               '</span></th></tr>';
            });
            innerHtml += '</thead><tbody>';

            bodyLines.forEach(function (body, i) {
              let colors = tooltipModel.labelColors[i];
              let style = 'background:' + colors.backgroundColor;
              style += '; border-color:' + colors.borderColor;
              style += '; border-width: 2px';
              let span = '<span style="' + style + '"></span>';
              innerHtml += '<tr><td style="background:white;border-radius:5px;color:#a05afa;padding-left:10px;padding-right:10px;font-size:16px;">' + span + formatNumber(body,2) + '%</td></tr>';
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
        yAxes: [{ 
          display: false,
          gridLines: {
            display: false,
            zeroLineColor: "#f3f3f5",
            color: "#f3f3f5",
          },
          min:0,
          max:100,
          ticks: {
            fontColor: '#c8c8d1',
            fontSize: 11,
            stepSize: 20,
            beginAtZero: true,
            callback: function(value, index, values) {
              return formatNumber(value, 2)
            }
          }
        }],
        xAxes: [{
          display: false,
          barThickness: 3.5,
          gridLines: {
            display: false,
            zeroLineColor: "white",
            color: "white",
          },
          ticks: {
            fontSize: 11,
            fontColor: '#c8c8d1',
            callback: function(value, index, values) {
              const arr = ThinBarGraphComponent.currentTimeZonelables[value].short.split("T");
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
    this.getBounceRateData();
    this.getBounceRateData();
    // this.interval = setInterval(() => {
    // }, CONFIGCONSTANTS.chartDataInterval);
  };

  processTwentyFourHoursData(data = null, label: String = 'A') {
    if (!data) {
      return [
        { data: [], label: label, borderWidth: 1.5 },
      ]
    }
    return [
      { data: getData(DurationType.TWENTY_FOUR_HOURS, data, 0), label: label, borderWidth: 1.5 },
    ]
  }
  getTwentyFourHoursLables() {
    return DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS);
  }
  getBounceRateData() {
    this.isPageLoading = true;
    this.liveService.getBounceRate().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.isPageLoading = false;
          this.chartData = processData(res,DurationType.TWENTY_FOUR_HOURS);
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
