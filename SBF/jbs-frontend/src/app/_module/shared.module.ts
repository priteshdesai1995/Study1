import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomDate } from '../_pipe/customDate.pipe';
import { FormatCkEditorObjectPipe } from '../_pipe/format-ck-editor-object.pipe';
import { TranslateLoader, TranslateModule, TranslatePipe } from '@ngx-translate/core';
import { HttpClient } from '@angular/common/http';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { PhoneMaskDirective } from '../_directives/phone-mask.directive';
import { NumberOnlyDirective } from '../_directives/number-only.directive';
import { UTCDatePipe } from '../_pipe/utcDate.pipe';
@NgModule({
  declarations: [CustomDate,UTCDatePipe, FormatCkEditorObjectPipe, PhoneMaskDirective, NumberOnlyDirective],
  imports: [
    CommonModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
  ],
  exports: [CustomDate,UTCDatePipe, FormatCkEditorObjectPipe, TranslatePipe, PhoneMaskDirective, NumberOnlyDirective],
})
export class SharedModule {}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}
