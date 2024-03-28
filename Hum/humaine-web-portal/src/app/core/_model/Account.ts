// Generated by https://quicktype.io

export interface Account {
    accountID:           number;
    fullName:            string;
    address:             string;
    phoneNumber:         string;
    city:                string;
    state:               string;
    country:             string;
    industry:            string[];
    businessURL:         string;
    headquarterLocation: string;
    cosumersFrom:        string;
    noOfEmployees:       string;
    noOfProducts:        string;
    highProductPrice:    string;
    curAnalyticsTool:    string;
    isUserDataTrack:     boolean;
    trackingProviders:   string[];
    trackerDataType:     string;
    expectationComments: string;
    homePageUrl:         string;
    detailedPage:        DetailedPage[];
    eshopHostedOn:       string;
}

export interface DetailedPage {
    pageURL:  string;
    pageName: string;
}
