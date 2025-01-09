import {Community} from './Community';

export class GiftAssignment {

  id: number;

  giverEmail: string;

  receiverEmail: string;

  community: Community;

  constructor(
    id: number,
    giverEmail: string,
    receiverEmail: string,
    community: Community
  ) {
    this.id = id;
    this.giverEmail = giverEmail;
    this.receiverEmail = receiverEmail;
    this.community = community;
  }
}
