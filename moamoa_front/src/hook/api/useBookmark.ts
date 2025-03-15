import {ServiceQueryType, useRepositoryQuery} from "../useCommon.ts";
import BookmarkRepository from "../../repository/BookmarkRepository.ts";
import {BookMarkResponse} from "../../model/Bookmark.ts";

// const instance = container.resolve(TestRepository);


export const useGetAllBookmarks = (param?: Record<string, unknown>,options?: Record<string, unknown>): ServiceQueryType<BookMarkResponse[]> => {
    return useRepositoryQuery<BookmarkRepository, BookMarkResponse[]>(BookmarkRepository, 'getAllBookmarks', [`getAllBookmarks`], param, options,);
};
