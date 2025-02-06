import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCounselComponent } from './admin-counsel.component';

describe('AdminCounselComponent', () => {
  let component: AdminCounselComponent;
  let fixture: ComponentFixture<AdminCounselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminCounselComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminCounselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
