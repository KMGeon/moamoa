// src/repositories/HttpRepository.ts
import {HttpClient} from "../utils/HttpClient.ts";
import {inject, singleton} from "tsyringe";
import {ApiResponse, isApiSuccess} from "../model/ApiResponse.ts";
import {HttpTypes} from "../utils/HttpTypes.ts";

@singleton()
class HttpRepository {
    private readonly httpClient: HttpClient;

    constructor(@inject(HttpClient) httpClient: HttpClient) {
        this.httpClient = httpClient;
    }

    async getData<T>({url, params}: {
        url: string;
        params?: Record<string, unknown>;
    }): Promise<T> {
        const response = await this.httpClient.getData<T>(url, params);
        return this.handleResponse<T>(response);
    }

    async postData<T, D = Record<string, unknown>>({url, data, params}: {
        url: string;
        data: D;
        params?: Record<string, unknown>;
    }): Promise<T> {
        const response = await this.httpClient.postData<T>(url, data, params);
        return this.handleResponse<T>(response);
    }

    async patchData<T, D = Record<string, unknown>>({url, data, params}: {
        url: string;
        data: D;
        params?: Record<string, unknown>;
    }): Promise<T> {
        const processedData = data != null ? this.ensureRecord(data) : null;
        const response = await this.httpClient.patchData<T>(url, processedData, params);
        return this.handleResponse<T>(response);
    }

    async deleteData<T>({url, data, params}: {
        url: string;
        data?: Record<string, unknown>;
        params?: Record<string, unknown>;
    }): Promise<T> {
        const processedData = data ? this.ensureRecord(data) : undefined;
        const response = await this.httpClient.deleteData<T>(url, processedData, params);
        return this.handleResponse<T>(response);
    }

    private handleResponse<T>(response: ApiResponse<T>): T {
        if (isApiSuccess(response)) {
            return response.data;
        } else {
            throw new HttpTypes(
                response.code,
                response.message || ''
            );
        }
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