import {inject, singleton} from "tsyringe";
import HttpRepository from "./HttpRepository.ts";
import {URL} from "../config/url.ts";


@singleton()
export default class TestRepository {

    constructor(@inject(HttpRepository) private readonly httpRepository: HttpRepository) {
    }


    public async findTest(param?: Record<string, unknown>) {
        return await this.httpRepository.getData({
            url: URL.test,
            params: param,
        })
    }

}
