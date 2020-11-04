import { Moment } from 'moment';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { IChat } from 'app/shared/model/chat.model';

export interface IPoruka {
  id?: number;
  text?: string;
  datum?: string;
  postoji?: boolean;
  dodatniInfoUser?: IDodatniInfoUser;
  chat?: IChat;
}

export const defaultValue: Readonly<IPoruka> = {
  postoji: false,
};
