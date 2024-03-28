import { DurationType, processData } from './../../_utility/common';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { LiveService } from '../../../pages/live/live.service';
import { CONFIGCONSTANTS } from '../../_constant/app-constant';
import { errorHandler, formatNumber, getTwentyFourHoursLables, processTwentyFourHoursData, successHandler } from '../../_utility/common';
import { ToasterService } from '../../_utility/notify.service';
import DateUtils from '../../_utility/date';

@Component({
  selector: 'line-graph',
  templateUrl: './line-graph.component.html',
  styleUrls: ['./line-graph.component.scss']
})
export class LineGraphComponent implements OnInit {
  @ViewChild('myCanvas')
  public canvas: ElementRef;
  public context: CanvasRenderingContext2D;
  public chartType: string = 'line';
  public chartData: any[] = processData(null,DurationType.TWENTY_FOUR_HOURS, '');
  public static graphDate: string = "";
  public chartLabels: any[] = DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS);
  static currentTimeZonelables: {} = DateUtils.getCurrentTimeZoneLables(DurationType.TWENTY_FOUR_HOURS);
  public chartColors: any[];
  public chartOptions: any;
  isPageLoading: boolean = false;
  interval: any;
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
  constructor(private liveService: LiveService, private toaster: ToasterService) { }

  ngOnInit() {
    this.chartLabels = DateUtils.getLables(DurationType.TWENTY_FOUR_HOURS);
    LineGraphComponent.currentTimeZonelables = DateUtils.getCurrentTimeZoneLables(DurationType.TWENTY_FOUR_HOURS);
    this.chartColors = [{
      backgroundColor: 'rgb(0 0 0 / 0%)',
      borderColor: '#a05afa',
      borderWidth: 2,
      pointBackgroundColor: 'white',
      pointBorderColor: '#a05afa',
      pointHoverBackgroundColor: 'white',
      pointHoverBorderColor: '#a05afa'
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

            let innerHtml = '<thead style="background:white;border-radius:10px;"><span style="padding-left:10px;padding-right:10px;font-family:SofiaProBold, sans-serif !important;font-size:13px;">Users</span>';

            titleLines.forEach(function (title) {
              innerHtml += '<tr><th style="background:white;border-radius:10px;padding-top:10px;padding-left:10px;padding-right:10px;color:#9999a9;"><img style="height:12px;" src="/assets/img/calendar-line.svg"><span style="padding-left:5px;font-size: 11px;font-weight: normal;">' + ((LineGraphComponent.currentTimeZonelables[tooltipModel.dataPoints[0].xLabel].long)) + '</span></th></tr>';
            });
            innerHtml += '</thead><tbody>';
            bodyLines.forEach(function (body, i) {
              let colors = tooltipModel.labelColors[i];
              let style = 'background:' + colors.backgroundColor;
              style += '; border-color:' + colors.borderColor;
              style += '; border-width: 2px';
              let span = '<span style="' + style + '"></span>';
              innerHtml += '<tr><td style="background:white;border-radius:10px;color:#a05afa;padding-left:10px;padding-right:10px;padding-bottom:5px;font-size:16px;">' + span + formatNumber(body, 2) + '</td></tr>';
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
            // stepValue: 10,  stepSize: 5,
            fontColor: '#c8c8d1',
            callback: function (value, index, values) {
              return formatNumber(value, 2)
            }
          }
        }],
        xAxes: [{
          gridLines: {
            display: true,
            zeroLineColor: "#f3f3f5",
            color: "#f3f3f5"
          },
          ticks: {
            fontColor: '#c8c8d1',
            callback: function(value, index, values) {
              const arr = LineGraphComponent.currentTimeZonelables[value].short.split("T");
              return arr[1];
            }
          }
        }]
      },
      plugins: {
        datalabels: {
          display: false,
        }
      }
    }
    
    this.getUserHourly();
    this.getUserHourly();
    // this.interval = setInterval(() => {
    // }, CONFIGCONSTANTS.chartDataInterval);
  };

  getUserHourly() {
    this.liveService.getHourlyDashboard().subscribe((res: any) => {
      successHandler(
        this.toaster,
        res,
        null,
        (isSuccess: boolean) => {
          this.chartData = processData(res.data,DurationType.TWENTY_FOUR_HOURS, '')
          LineGraphComponent.graphDate = res.date;
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
