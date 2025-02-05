import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TermDetailsDialogComponent } from './term-details-dialog.component';

describe('TermDetailsDialogComponent', () => {
  let component: TermDetailsDialogComponent;
  let fixture: ComponentFixture<TermDetailsDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TermDetailsDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TermDetailsDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
