import { Component, OnInit, Input } from '@angular/core';
import { CONFIGCONSTANTS, PersonaCardTitle } from '../../_constant/app-constant';
import { DetailsData } from '../../_model/persona';

@Component({
  selector: 'persona-card',
  templateUrl: './persona-card.component.html',
  styleUrls: ['./persona-card.component.scss']
})
export class PersonaCardComponent implements OnInit {
 noOfVisibleItems: number = CONFIGCONSTANTS.CardVisibleNo;
 personalCardKeys = CONFIGCONSTANTS.PersonaCardTooltip;
  @Input() data: any;
  @Input() title: string = '';
  @Input() image: string = '';
  @Input() mustContainsBullets: boolean = false;
  isPersuasiveShow = false;
  constructor() { }

  ngOnInit(): void {
    if(this.title === PersonaCardTitle.personality){
      this.noOfVisibleItems = 2;
    }
  }

  get showBullets() {
    return this.data.data.length > 1;
  }

  showMore(e:DetailsData){
    e.showMore = !e.showMore
  }

  getTooltipText(title){
    return title;
  }
}
