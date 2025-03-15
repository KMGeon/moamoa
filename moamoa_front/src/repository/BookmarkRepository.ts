import {inject, singleton} from "tsyringe";
import HttpRepository from "./HttpRepository.ts";
import {URL} from "../config/url.ts";
import {ApiResponse} from "../model/ApiResponse.ts";


@singleton()
export default class BookmarkRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }

    public async getAllBookmarks(param?: Record<string, unknown>): Promise<ApiResponse<any>> {
        return await this.httpRepository.getData<any>({
            url: URL.bookmark,
            params: param,
        });
    }

}
