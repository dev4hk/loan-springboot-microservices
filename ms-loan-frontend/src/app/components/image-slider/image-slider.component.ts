import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { ImageSlideInterface } from './image-slide-interface';
import { slideAnimation } from './animations';

@Component({
  selector: 'app-image-slider',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './image-slider.component.html',
  styleUrls: ['./image-slider.component.scss'],
  animations: [slideAnimation],
})
export class ImageSliderComponent {
  @Input() slides: Array<ImageSlideInterface> = [];
  currentIndex: number = 0;

  getCurrentSlideUrl(): string {
    return `url('${this.slides[this.currentIndex].url}')`;
  }

  goToNext(): void {
    const isLastSlide = this.currentIndex === this.slides.length - 1;
    this.currentIndex = isLastSlide ? 0 : this.currentIndex + 1;
  }

  goToPrevious(): void {
    const isFirstSlide = this.currentIndex === 0;
    this.currentIndex = isFirstSlide
      ? this.slides.length - 1
      : this.currentIndex - 1;
  }

  goToSlide(slideIndex: number): void {
    this.currentIndex = slideIndex;
  }
}
