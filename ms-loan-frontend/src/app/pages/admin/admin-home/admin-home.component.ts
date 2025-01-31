import { Component } from '@angular/core';
import { DonutChartComponent } from '../../../components/donut-chart/donut-chart.component';
import { DonutChartInterface } from '../../../components/donut-chart/donut-chart-interface';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-home',
  imports: [DonutChartComponent, RouterModule],
  templateUrl: './admin-home.component.html',
  styleUrl: './admin-home.component.scss',
})
export class AdminHomeComponent {
  data1: DonutChartInterface = {
    title: 'title1',
    data: [{ label: 'data1', number: 1 }],
  };
  data2: DonutChartInterface = {
    title: 'title2',
    data: [{ label: 'data2', number: 1 }],
  };
  data3: DonutChartInterface = {
    title: 'title3',
    data: [{ label: 'data3', number: 1 }],
  };
  data4: DonutChartInterface = {
    title: 'title4',
    data: [{ label: 'data4', number: 1 }],
  };
}
