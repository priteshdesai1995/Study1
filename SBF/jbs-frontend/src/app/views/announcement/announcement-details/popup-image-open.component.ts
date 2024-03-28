import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
  selector: 'app-popup-image-open',
  template: `
    <div class="modal-content">
      <div class="modal-header bg-primary">
        <h5 class="modal-title">
          <i class="fa fa-picture-o modal-icon"></i>
          {{ 'IMAGE' | translate }}
        </h5>
        <button type="button" class="close" (click)="decline()">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <div class="row">
          <img src="{{ displayImage }}" class="img-fluid" />
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-warning" (click)="decline()">
          {{ 'CLOSE' | translate }}
        </button>
      </div>
    </div>
  `,
  styleUrls: [],
})
export class PopupImageOpenComponent implements OnInit {
  image: string;
  displayImage: string;

  constructor(public modalRef: BsModalRef) {}
  ngOnInit() {
    this.getImage();
  }

  getImage() {
    if (this.image) {
      this.displayImage = this.image;
    }
  }
  decline(): void {
    this.modalRef.hide();
  }
}
