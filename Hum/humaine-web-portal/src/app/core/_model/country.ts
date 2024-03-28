export interface IState {
    name: string;
    isoCode: string;
    countryCode: string;
    latitude?: string | null;
    longitude?: string | null;
}
export interface ICity {
    name: string;
    countryCode: string;
    stateCode: string;
    latitude?: string | null;
    longitude?: string | null;
}