import { Component } from '@angular/core';
import { DonutChartComponent } from "../../components/donut-chart/donut-chart.component";

@Component({
  selector: 'app-home',
  imports: [DonutChartComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
