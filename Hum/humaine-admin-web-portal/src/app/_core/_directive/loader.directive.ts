import {
    Directive,
    ElementRef,
    Input,
    OnChanges,
    OnInit,
    Renderer2,
    TemplateRef,
  } from '@angular/core';
  
  @Directive({
    selector: '[appLoader]',
  })
  export class LoaderDirective implements OnInit, OnChanges {
    @Input() loading = false;
    @Input() isDataTable = false;
    @Input() text = '';
    @Input() size = 30;
    @Input() background = 'transparent';
    @Input() color = '#01685c';
    @Input() loadFromButtonHtml = false;
    @Input() showButtonHtmlWhileLoading = false;
    @Input() load = false;
    innerHtml = '';
    initCalled = false;
    constructor(private el: ElementRef, private renderer: Renderer2) {
      this.initCalled = false;
    }
  
    setText() {
      if (!this.initCalled) return;
      let html = '';
      if (this.loadFromButtonHtml === true && (!this.loading || this.loading && this.showButtonHtmlWhileLoading)) {
        html = this.innerHtml;
      }
      html = this.getImage + html + this.text;
      this.el.nativeElement.innerHTML = html;
    }
  
    get imageLink() {
      return 'assets/img/spinner.gif';
    }
    get getImage() {
      if (!this.loading) {
        return '';
      }
      return (
        '<img src="' +
        this.imageLink +
        '" width="' +
        this.size +
        '"/>&nbsp;'
      );
    }
  
    ngOnInit() {
      this.initCalled = true;
      this.innerHtml = this.el.nativeElement.innerHTML;
      this.setText();
    }
    ngOnChanges() {
      this.setText();
    }
  }
  