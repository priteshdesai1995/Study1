import { Component, OnInit, ViewChild } from '@angular/core';
import { DataTableConfig } from '../../core/_model/DataTableConfig';
import { URLS } from '../../core/_constant/api.config';
import { APIType } from '../../core/_model/APIType';
import { DataTableColumn } from '../../core/_model/DataTableColumn';
import { CONFIGCONSTANTS, BigFive, SortDirection } from '../../core/_constant/app-constant';
import { ListTableComponent } from '../../core/_component/list-table/list-table.component';

@Component({
  selector: 'app-ai-generated-user-group',
  templateUrl: './ai-generated-user-group.component.html',
  styleUrls: ['./ai-generated-user-group.component.scss']
})
export class AIGeneratedUserGroupComponent implements OnInit {
  @ViewChild("savedAIUserList") savedAIUserList: ListTableComponent;
  @ViewChild("aIUserList") aIUserList: ListTableComponent;

  AIGeneratedTableConfig: DataTableConfig = new DataTableConfig(URLS.aiGeneratedUserGroupList, APIType.GET, URLS.deleteAiGeneratedUserGroup, 'ai/viewPersona/', 'AI Generated User Groups', [
    new DataTableColumn("GROUP NAME", "name", false, true, "-", null, true),
    new DataTableColumn("BIG FIVE", "bigFive"),
    new DataTableColumn("VALUES", "values"),
    new DataTableColumn("PERSUASIVE STRATERGY	", "persuasiveStratergies"),
    new DataTableColumn("SUCCESS MATCH", "successMatch", false, true, '-', '%'),
    new DataTableColumn("USERS", "noOfUser", false, true, '-')

  ], [
    ...CONFIGCONSTANTS.sortByList,
    { name: "Values", value: BigFive.VALUES },
    { name: "Persuasive Stratergies", value: BigFive.PERSUASIVE_STRATERGY },
    { name: "User", value: BigFive.NOOFUSER }

  ], "Delete User Group?", "Do you want to delete this user group?", "Yes, Delete This User Group", 'groupId', SortDirection.DESC, false, false, true, true, true, true, '550px', '1000px', false);

  SavedAIUserGroupTableConfig: DataTableConfig = new DataTableConfig(URLS.savedAiGeneratedUserGroupList, APIType.GET, URLS.deletesavedAiGeneratedUserGroup, 'saved/viewPersona/', 'Saved AI User Groups', [
    new DataTableColumn("", "icon", true),
    new DataTableColumn("GROUP NAME", "name"),
    new DataTableColumn("BIG FIVE", "bigFive"),
    new DataTableColumn("SUCCESS MATCH", "successMatch", false, true, '-', '%'),
    new DataTableColumn("USERS", "noOfUser", false, true, '-')


  ], [...CONFIGCONSTANTS.sortByList,
  { name: "User", value: BigFive.USERS }
  ], "Delete User Group?", "Do you want to delete this user group?", "Yes, Delete This User Group", 'id', SortDirection.DESC, false, false, false, false, false, true, '550px', '1000px');

  constructor() { }

  ngOnInit(): void {
  }

  onUserGroupSave() {
    this.savedAIUserList.rerender();

  }

  onUserGroupDelete() {
    this.aIUserList.rerender();
  }

}
