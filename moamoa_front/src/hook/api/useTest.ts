import TestRepository from "../../repository/TestRepository.ts";
import {ServiceQueryType, useRepositoryQuery} from "../useCommon.ts";

// const instance = container.resolve(TestRepository);


export const useTest1 = (param?: Record<string, unknown>,options?: Record<string, unknown>): ServiceQueryType<any> => {
    return useRepositoryQuery<TestRepository, any>(TestRepository, 'findTest', [`test1`], param, options);
};