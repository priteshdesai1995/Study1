import { ChartType } from './../../_constant/app-constant';
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, Label } from 'ng2-charts';
import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-graph',
  templateUrl: './graph.component.html',
  styleUrls: ['./graph.component.scss']
})
export class GraphComponent implements OnInit {
  @ViewChild('graphCanvas') canvas;
  @Input() data: any = [];
  backGroundColor: any = '';
  readonly colors = [{
    color: 'rgba(252,240,241,1)',
    stop: 0
  },
  {
    color: 'rgba(255,255,255,1)',
    stop: 1
  }
  ]
  @Input() lineChartData: ChartDataSets[] = [
    { data: this.data, label: 'Series A', borderWidth: 1.5 },
  ];
  @Input() lineChartLabels: Label[] = []
  @Input() type: ChartType;
  public getColor() {
    switch (this.type) {
      case ChartType.INCREASE:
        return '#03e28b';
      case ChartType.DECREASE:
        return '#ff6767';
      default:
        return '#03e28b';
    }
  }

  public getShadowColor() {
    switch (this.type) {
      case ChartType.INCREASE:
        return '#03e28b';
      case ChartType.DECREASE:
        return '#ff5f5f';
      default:
        return '#03e28b';
    }
  }

  public lineChartOptions: (ChartOptions) = {
    animation: {
      duration: 0 // general animation time
    },
    hover: {
      animationDuration: 0 // duration of animations when hovering an item
    },
    responsiveAnimationDuration: 0,// animation duration after a resize
    maintainAspectRatio: false,
    responsive: true,
    tooltips:{
      enabled:false
    },
    elements: {
      point: { radius: 0 }
    },
    legend: {
      display: false
    },
    scales: {
      xAxes: [{
        display: false,
        gridLines: {
          display: false
        }
      }],
      yAxes: [{
        display: false,
        gridLines: {
          display: false
        }
      }]
    },
    plugins: {
      datalabels: {
        display: false,
      },
    }
  };
  public lineChartLegend = true;
  public lineChartType = 'line';
  public lineChartPlugins = [
    {
      afterLayout: chart => {
        const ctx = chart.chart.ctx;
        const canvasEle = ctx.canvas
        const xAxis = chart.scales['x-axis-0'];
        const gradientStroke = ctx.createLinearGradient(
          canvasEle.width / 2, 0, canvasEle.width / 2, canvasEle.height
        );
        const dataset = chart.data.datasets[0];
        this.colors.forEach((c, i) => {
          gradientStroke.addColorStop(c.stop, c.color);
        });
        dataset.backgroundColor = gradientStroke;
      },
      afterDatasetUpdate: (chartInstance: Chart, options?: any) => {
        
        const ctx = chartInstance.ctx;
        let _stroke = ctx.stroke;
        ctx.stroke = function () {
          ctx.save();
          switch (this.type) {
            case ChartType.INCREASE:
              ctx.shadowColor = '#03e28b';
            case ChartType.DECREASE:
              ctx.shadowColor = '#ff5f5f';
          }
          ctx.shadowBlur = 10;
          ctx.shadowOffsetX = 0;
          ctx.shadowOffsetY = 5;
          _stroke.apply(this, arguments)
          ctx.restore();
        }
      }
    }
  ];

  constructor() {
  }

  ngOnInit(): void {
  }
}

// 