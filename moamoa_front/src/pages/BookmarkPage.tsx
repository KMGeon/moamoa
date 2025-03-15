import LoadingHandler from "../components/LoadingHandlerProps.tsx";
import {useGetAllBookmarks} from "../hook/api/useBookmark.ts";


export default function BookmarkPage() {
    const {data, isLoading} = useGetAllBookmarks();
    console.log(data);

    return (
        <LoadingHandler isLoading={isLoading} fallback={<div>Loading...</div>}>
            <div>
                1
            </div>
        </LoadingHandler>
    );
}

