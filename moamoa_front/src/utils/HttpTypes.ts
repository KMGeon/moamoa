export class HttpTypes extends Error {
  constructor(
    public code?: string,
    message?: string,
  ) {
    super(message);
    this.name = 'HttpTypes';
  }
}


import { AxiosRequestConfig, AxiosResponse } from 'axios';

export type RequestInterceptor = (config: AxiosRequestConfig) => AxiosRequestConfig | Promise<AxiosRequestConfig>;
export type ResponseInterceptor = (response: AxiosResponse) => AxiosResponse | Promise<AxiosResponse>;

export interface HttpRequestConfig {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH';
  url: string;
  params?: Record<string, unknown> | string;
  data?: unknown;
  headers?: Record<string, string>;
}