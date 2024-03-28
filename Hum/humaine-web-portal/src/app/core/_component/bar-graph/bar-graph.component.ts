import { Component, HostListener, Injectable, Input, OnInit } from '@angular/core';
import { ChartOptions, ChartType, ChartDataSets } from 'chart.js';
import { Label, Color } from 'ng2-charts';
import * as pluginDataLabels from 'chartjs-plugin-datalabels';
import { eventCounts } from '../../_model/UXInsight';
import { UxInsightBar } from '../../_constant/app-constant';

@Component({
  selector: 'bar-graph',
  templateUrl: './bar-graph.component.html',
  styleUrls: ['./bar-graph.component.scss']
})

export class BarGraphComponent {

  @HostListener('window:resize', ['$event'])
  getScreenSize(event?) {
    if (window.innerWidth <= 1366) {
      this.barChartOptions.scales.maintainAspectRatio = false;
    }
  }
  _data: [];

  @Input() set data(data: any) {
    this._data = data;
    this.setData();
  }

  labels: UxInsightBar;
  title = 'bar-chart';
  barChartOptions: any = {
    animation: {
      duration: 0 // general animation time
    },
    hover: {
      animationDuration: 0 // duration of animations when hovering an item
    },
    responsiveAnimationDuration: 0,// animation duration after a resize
    responsive: true,
    maintainAspectRatio: true,
    aspectRatio: 1.2,
    layout: {
      padding: {
        top: 0
      }
    },
    tooltips: {
      enabled: false,
    },
    legend: {
      display: false
    },
    scales: {
      xAxes: [{
        gridLines: {
          display: true,
          zeroLineColor: "white",
          color: "white",
        },
        scaleLabel: {
          display: true,
          labelString: '',
          fontStyle: "bold",
          fontSize: 11,
          fontFamily: "'SofiaProMedium', 'sans-serif'",
          fontColor: '#39476c'
        },
        ticks: {
          callback: function (label) {
            if (window.innerWidth >= 1200 && /\s/.test(label)) {
              return label.split(" ");
            } else {
              return label;
            }
          },
          autoSkip: false,
          maxRotation: this.getMaxRotation(),
          minRotation: this.getMinRotation(),
          fontSize: 11,
          stepSize: 1,
          beginAtZero: true,
          fontColor: "#39476c",
          fontFamily: "'SofiaProMedium', 'sans-serif'",
        },
        barThickness: 25,

      }],
      yAxes: [{
        spaccing: 50,
        gridLines: {
          display: true,
          zeroLineColor: "#f3f3f5",
          color: "#f3f3f5"
        },
        scaleLabel: {
          display: true,
          labelString: 'Total percent',
          fontColor: "darkgray",
          fontSize: 11,
          fontFamily: "'SofiaProMedium', 'sans-serif'",
        },
        ticks: {
          beginAtZero: true,
          display: true,
          stepSize: 10,
          fontColor: "darkgray",
          fontFamily: "'SofiaProMedium', 'sans-serif'",
        },
      }]
    },
    plugins: {
      datalabels: {
        anchor: 'end',
        align: 'end',
        color: "#39476c",
        font: {
          size: 10,
          weight: 600,
          family: "'SofiaProMedium', 'sans-serif'"
        },
        formatter: (value, context) => {
          let percent = Math.round(value * 10) / 10;
          return `${percent}%`;
        },
      },
    }
  };
  barChartLabels: Label[] = [];
  barChartType: ChartType = 'bar';
  barChartLegend = true;
  barChartPlugins: any = [pluginDataLabels];
  barChartData: ChartDataSets[] = [];
  public barChartColors: Color[] = [

  ]
  lineChartPlugins = [
    {
      afterDraw: chart => {
        let ctx = chart.chart.ctx;
        let xAxis = chart.scales['x-axis-0'];
        let yAxis = chart.scales['y-axis-0'];
        let gradientStroke = ctx.createLinearGradient(0, 0, 0, 200);
        let dataset = chart.data.datasets[0];

        gradientStroke.addColorStop(0, '#02decc');
        gradientStroke.addColorStop(1, '#60e7f2');
        this.barChartColors.push(gradientStroke);
        dataset.backgroundColor = gradientStroke;
        dataset.borderColor = gradientStroke;
        dataset.pointBorderColor = gradientStroke;
        dataset.hoverBorderColor = gradientStroke;
        dataset.hoverBackgroundColor = gradientStroke;

      }
    }
  ];
  ngOnit() {
    this.setData();
  }
  setData() {
    const data = [];
    if (this._data && this._data.length > 0) {
      this._data.forEach((element: any) => {
        data.push(element.percentage || 0);
        this.barChartLabels.push(UxInsightBar[element.eventName]);
      });
    }
    // if (this._data && Object.keys(this._data).length > 0) {
    //   Array.prototype.push.apply(data, [this._data.productViewPercentage || 0, 0, this._data.blogPostEventPercentage || 0, this._data.wishListPercentage || 0, this._data.reviewPercentage || 0, this._data.buyPercentage || 0, this._data.otherPercentage || 0]);
    // }
    // data.splice(-2);
    // this.barChartLabels.splice(-2);
    this.barChartData = [
      {
        data: [...data],
        maxBarThickness: 20
      }
    ]
  }
  getMaxRotation() {
    return window.innerWidth < 400 ? 45 : 0;
  }

  getMinRotation() {
    return window.innerWidth < 400 ? 45 : 0;
  }
}
