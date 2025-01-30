import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEntryComponent } from './admin-entry.component';

describe('AdminEntryComponent', () => {
  let component: AdminEntryComponent;
  let fixture: ComponentFixture<AdminEntryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminEntryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
