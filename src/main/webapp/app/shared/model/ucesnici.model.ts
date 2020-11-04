import { IChat } from 'app/shared/model/chat.model';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';

export interface IUcesnici {
  id?: number;
  chat?: IChat;
  dodatniInfoUsers?: IDodatniInfoUser[];
}

export const defaultValue: Readonly<IUcesnici> = {};
