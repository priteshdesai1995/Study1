import { Component, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { ActivatedRoute } from "@angular/router";
import { CmsService } from "../../../_services/cms.service";
import { first } from "rxjs/operators";

@Component({
  selector: "app-cms-component-view",
  templateUrl: "./cms-component-view.component.html",
  styleUrls: ["./cms-component-view.component.scss"],
})
export class CmsComponentViewComponent implements OnInit {
  cmsId: any;
  private routeSub: Subscription;
  editMode: Boolean = false;
  private editCmsId: number;
  htmldata;
  constructor(private cmsService: CmsService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.routeSub = this.route.params.subscribe((params) => {
      this.cmsId = params["id"];
      this.editMode = params["id"] != null;
      setTimeout(() => {
        this.getTemplateData();
      }, 100);
    });
  }
  getTemplateData() {
    if (this.editMode) {
      this.cmsService
        .getCmsComponentTemplate(this.cmsId)
        .pipe(first())
        .subscribe(
          (response) => {
            this.htmldata = response.responseData["gjs-html"];
          },
          (error) => {
            console.log(error);
          }
        );
    }
  }
}
