import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-counsel',
  imports: [],
  templateUrl: './counsel.component.html',
  styleUrl: './counsel.component.scss',
})
export class CounselComponent implements OnInit {
  ngOnInit(): void {
    this.addToggleFunctionality();
  }

  addToggleFunctionality(): void {
    const faqQuestions = document.querySelectorAll('.faq-question');

    faqQuestions.forEach((question) => {
      question.addEventListener('click', () => {
        const answer = question.nextElementSibling as HTMLElement;
        if (answer) {
          if (answer.classList.contains('open')) {
            answer.classList.remove('open');
          } else {
            document.querySelectorAll('.faq-answer').forEach((el) => {
              el.classList.remove('open');
            });
            answer.classList.add('open');
          }
        }
      });
    });
  }
}
