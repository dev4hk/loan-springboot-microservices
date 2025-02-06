import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CounselComponent } from './counsel.component';

describe('CounselComponent', () => {
  let component: CounselComponent;
  let fixture: ComponentFixture<CounselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CounselComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CounselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
