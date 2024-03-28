import { Address } from "./address";

export class RegisterRequest {
    public firstName: string;
    public middleName: string;
    public lastName: string;
    public userName: string;
    public password: string;
    public dateOfBirth: string;
    public gender: string;
    public email: string;
    public occupation: string;
    public cellPhone: number;
    public employer: number;
    public addressDTOList: Address;
    constructor(data: any) {
        this.firstName = data.firstName;
        this.middleName = data.middleName;
        this.lastName = data.lastName;
        this.userName = data.userName;
        this.email = data.email;
        this.dateOfBirth = data.dob;
        this.cellPhone = data.mobileNo;
        this.gender = data.gender;
        this.password = data.password;
        this.occupation = data.occupation;
        this.employer = data.employer;
        if (data.addressDTOList != null) {
            this.addressDTOList = new Address(data.addressDTOList);
        }
    }
}
