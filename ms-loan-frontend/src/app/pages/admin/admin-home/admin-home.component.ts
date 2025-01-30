import { Component } from '@angular/core';
import { DonutChartComponent } from '../../../components/donut-chart/donut-chart.component';
import { DonutChartInterface } from '../../../components/donut-chart/donut-chart-interface';

@Component({
  selector: 'app-admin-home',
  imports: [DonutChartComponent],
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.scss',
})
export class AdminHomeComponent {
  data1: Array<DonutChartInterface> = [{ label: 'data1', number: 1 }];
  data2: Array<DonutChartInterface> = [{ label: 'data2', number: 1 }];
  data3: Array<DonutChartInterface> = [{ label: 'data3', number: 1 }];
  data4: Array<DonutChartInterface> = [{ label: 'data4', number: 1 }];
}
