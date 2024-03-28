export class IResponse<T> {
    status:       string;
    responseData: T;
    statusCode:   number;
    errorList:    ErrorList[];
}

export interface ErrorList {
    code:    string;
    message: string;
}