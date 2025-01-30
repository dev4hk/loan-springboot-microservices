import {
  Component,
  Input,
  OnChanges,
  SimpleChanges,
  OnInit,
} from '@angular/core';
import { ApexLegend, NgApexchartsModule } from 'ng-apexcharts';
import {
  ApexNonAxisChartSeries,
  ApexResponsive,
  ApexChart,
  ApexFill,
  ApexTooltip,
  ApexPlotOptions,
} from 'ng-apexcharts';
import { DonutChartInterface } from './donut-chart-interface';

@Component({
  selector: 'app-donut-chart',
  imports: [NgApexchartsModule],
  templateUrl: './donut-chart.component.html',
  styleUrl: './donut-chart.component.scss',
})
export class DonutChartComponent implements OnChanges {
  @Input() data: Array<DonutChartInterface> = [];
  @Input() title: string = '';

  public chartOptions!: {
    series: ApexNonAxisChartSeries;
    chart: ApexChart;
    responsive: ApexResponsive[];
    labels: string[];
    fill: ApexFill;
    tooltip: ApexTooltip;
    plotOptions: ApexPlotOptions;
    title: ApexTitleSubtitle;
    legend: ApexLegend;
  };

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && this.data.length) {
      this.updateChart();
    }
  }

  private updateChart() {
    this.chartOptions = {
      title: {
        text: this.title,
        style: { color: '#29D0B2' },
        margin: 20,
      },
      legend: { show: false },
      series: this.data.map((item) => item.number),
      labels: this.data.map((item) => item.label),
      chart: {
        type: 'donut',
        width: 330,
        height: 330,
      },
      responsive: [
        {
          breakpoint: 480,
          options: {
            chart: { width: 200 },
            legend: { position: 'bottom' },
          },
        },
      ],
      fill: { type: 'gradient' },
      tooltip: {
        y: {
          formatter: (val: number) => `${val}%`,
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
                formatter: (data) => `${data}%`,
                color: '#29D0B2',
              },
            },
          },
        },
      },
    };
  }
}
