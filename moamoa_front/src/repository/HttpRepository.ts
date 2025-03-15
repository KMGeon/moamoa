// src/repositories/HttpRepository.ts
import { HttpClient } from "../utils/HttpClient.ts";
import { inject, singleton } from "tsyringe";
import {ApiResponse} from "../model/ApiResponse.ts";

@singleton()
class HttpRepository {
    private readonly httpClient: HttpClient;

    constructor(@inject(HttpClient) httpClient: HttpClient) {
        this.httpClient = httpClient;
    }

    async getData<T>({url, params}: {
        url: string;
        params?: Record<string, unknown>;
    }): Promise<ApiResponse<T>> {
        return this.httpClient.getData<T>(url, params);
    }

    async postData<T, D = Record<string, unknown>>({url, data, params}: {
        url: string;
        data: D;
        params?: Record<string, unknown>;
    }): Promise<ApiResponse<T>> {
        return this.httpClient.postData<T>(url, data, params);
    }

    async patchData<T, D = Record<string, unknown>>({url, data, params}: {
        url: string;
        data: D;
        params?: Record<string, unknown>;
    }): Promise<ApiResponse<T>> {
        const processedData = data != null ? this.ensureRecord(data) : null;
        return this.httpClient.patchData<T>(url, processedData, params);
    }

    async deleteData<T>({url, data, params}: {
        url: string;
        data?: Record<string, unknown>;
        params?: Record<string, unknown>;
    }): Promise<ApiResponse<T>> {
        const processedData = data ? this.ensureRecord(data) : undefined;
        return this.httpClient.deleteData<T>(url, processedData, params);
    }

    private ensureRecord<D>(data: D): Record<string, unknown> {
        if (typeof data === 'object' && data !== null) {
            return Object.entries(data).reduce((acc, [key, value]) => {
                acc[key] = value;
                return acc;
            }, {} as Record<string, unknown>);
        }
        throw new Error('Invalid data type. Expected an object.');
    }
}

export default HttpRepository;