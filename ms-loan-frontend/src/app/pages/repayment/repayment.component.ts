import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-repayment',
  imports: [],
  templateUrl: './repayment.component.html',
  styleUrl: './repayment.component.scss',
})
export class RepaymentComponent implements OnInit {
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
