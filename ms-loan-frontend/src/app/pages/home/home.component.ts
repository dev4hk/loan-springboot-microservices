import { Component } from '@angular/core';
import { DonutChartComponent } from '../../components/donut-chart/donut-chart.component';
import { DonutChartInterface } from '../../components/donut-chart/donut-chart-interface';

@Component({
  selector: 'app-home',
  imports: [DonutChartComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  data1: DonutChartInterface = {
    title: 'title1',
    data: [
      { label: 'data1', number: 10 },
      { label: 'data2', number: 20 },
    ],
  };
  data2: DonutChartInterface = {
    title: 'title2',
    data: [
      { label: 'data2', number: 10 },
      { label: 'data2', number: 20 },
    ],
  };
}
