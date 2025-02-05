import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminTermsComponent } from './admin-terms.component';

describe('AdminTermsComponent', () => {
  let component: AdminTermsComponent;
  let fixture: ComponentFixture<AdminTermsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTermsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminTermsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
