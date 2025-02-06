import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { ApexLegend, NgApexchartsModule } from 'ng-apexcharts';
import {
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexChart,
  ApexFill,
  ApexTooltip,
  ApexPlotOptions,
  ApexTitleSubtitle,
} from 'ng-apexcharts';
import { DonutChartInterface } from './donut-chart-interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-donut-chart',
  standalone: true,
  imports: [NgApexchartsModule, CommonModule],
  templateUrl: './donut-chart.component.html',
  styleUrl: './donut-chart.component.scss',
})
export class DonutChartComponent implements OnChanges {
  @Input() data: DonutChartInterface = { title: '', data: [] };

  public chartOptions: {
    series: ApexNonAxisChartSeries;
    chart: ApexChart;
    responsive: ApexResponsive[];
    labels: string[];
    fill: ApexFill;
    tooltip: ApexTooltip;
    plotOptions: ApexPlotOptions;
    title: ApexTitleSubtitle;
    legend: ApexLegend;
  } = {
    title: { text: '', style: { color: '#29D0B2' }, margin: 20 },
    legend: { show: false },
    series: [],
    labels: [],
    chart: { type: 'donut', width: 360, height: 360 },
    responsive: [
      {
        breakpoint: 480,
        options: {
          chart: { width: 250 },
          legend: { position: 'bottom' },
        },
      },
    ],
    fill: { type: 'gradient' },
    tooltip: {
      enabled: false,
      y: {
        formatter: (val: number) => `${val}`,
        title: { formatter: (seriesName: string) => `${seriesName}:` },
      },
    },
    plotOptions: {
      pie: {
        donut: {
          labels: {
            show: true,
            name: { show: true, offsetY: -10, color: '#29D0B2' },
            value: {
              show: true,
              formatter: (data) => `${data}`,
              color: '#29D0B2',
            },
          },
        },
      },
    },
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data']) {
      this.updateChart();
    }
  }

  private updateChart() {
    const hasData = this.data.data && this.data.data.length > 0;

    this.chartOptions = {
      title: {
        text: this.data.title,
        style: { color: '#29D0B2' },
        margin: 20,
      },
      legend: { show: false },
      series: hasData ? this.data.data.map((item) => item.number) : [1],
      labels: hasData ? this.data.data.map((item) => item.label) : ['No Data'],
      chart: { type: 'donut', width: 360, height: 360 },
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: { width: 250 },
            legend: { position: 'bottom' },
          },
        },
      ],
      fill: { type: 'gradient' },
      tooltip: {
        enabled: hasData,
        y: {
          formatter: (val: number) => (hasData ? `${val}` : 'No Data'),
          title: { formatter: (seriesName: string) => `${seriesName}:` },
        },
      },
      plotOptions: {
        pie: {
          donut: {
            labels: {
              show: true,
              name: { show: true, offsetY: -10, color: '#29D0B2' },
              value: {
                show: true,
                formatter: (data) => (hasData ? `${data}` : 'No Data'),
                color: '#29D0B2',
              },
            },
          },
        },
      },
    };
  }
}
