import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCounselDetailComponent } from './admin-counsel-detail.component';

describe('AdminCounselDetailComponent', () => {
  let component: AdminCounselDetailComponent;
  let fixture: ComponentFixture<AdminCounselDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCounselDetailComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCounselDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
