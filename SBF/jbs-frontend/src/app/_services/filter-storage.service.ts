import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class FilterStorageService {
  constructor() {}

  saveState(key, value) {
    sessionStorage.setItem(key, JSON.stringify(value));
  }
  getState(key, defaultValue) {
    const data = sessionStorage.getItem(key);
    if (data) {
      return JSON.parse(data);
    } else {
      return defaultValue;
    }
  }
  saveSingleState(key, value) {
    sessionStorage.setItem(key, value);
  }
  getSingleState(key, defaultValue) {
    const data = sessionStorage.getItem(key);
    if (data) {
      return data;
    } else {
      return defaultValue;
    }
  }
}
