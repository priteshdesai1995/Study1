// Generated by https://quicktype.io

export class IPersona {
    userCount: number = 0;
    ageGroup: string = "";
    state: string = "";
    familySize: string = null;
    gender: string = '';
    education: string = '';
    bigFive: string = '';
    job: string = '';
    income: string = '';
    details: Details = new Details();
    group: Group = new Group();
    groupsDetails: GroupsDetails = new GroupsDetails();
    popularProduts:any[]=[];
}
export class Details {
    personality: DetailsData = new DetailsData();
    persuasiveStrategies: DetailsData = new DetailsData();
    values: DetailsData = new DetailsData();
    goals: DetailsData = new DetailsData();
    motivationToBuy: DetailsData = new DetailsData();
    frustrations: DetailsData = new DetailsData();
}

export class DetailsData{
    showMore : boolean = false;
    data : any[] =[];
}
export interface MotivationToBuy {
    data: string;
    percentage: string;
}

export class Group {
    rank: number = 0;
    name: string = '';
}

export class GroupsDetails
{
    group: Group[] = [];
    ageGroup: string[] = [];
    familySize: string[] = [];
}
export class popularProduts{
    totalSoldQuantities: number = null;
    productId: string = '';
    productImage:string = null;
    productName: string = '';
}
 