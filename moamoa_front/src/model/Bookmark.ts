export class BookMarkResponse {
    constructor(
        public bookmarkUid: string = '',
        public url: string = '',
        public category: string = '',
        public createdAt: string = '',
    ) {
    }
}