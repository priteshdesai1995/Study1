export class Address {
    addressType: string;
    addressLineOne: string;
    addressLineTwo: string;
    city: string;
    province: string;
    postalCode: number;

    constructor(data: any) { 
        this.addressType = data.addressType;
        this.addressLineOne = data.addressLineOne;
        this.addressLineTwo = data.addressLineTwo;
        this.city = data.city;
        this.province = data.province;
        this.postalCode = data.postalCode;
    }
}