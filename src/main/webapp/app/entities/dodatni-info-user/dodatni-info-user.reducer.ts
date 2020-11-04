import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDodatniInfoUser, defaultValue } from 'app/shared/model/dodatni-info-user.model';

export const ACTION_TYPES = {
  SEARCH_DODATNIINFOUSERS: 'dodatniInfoUser/SEARCH_DODATNIINFOUSERS',
  FETCH_DODATNIINFOUSER_LIST: 'dodatniInfoUser/FETCH_DODATNIINFOUSER_LIST',
  FETCH_DODATNIINFOUSER: 'dodatniInfoUser/FETCH_DODATNIINFOUSER',
  CREATE_DODATNIINFOUSER: 'dodatniInfoUser/CREATE_DODATNIINFOUSER',
  UPDATE_DODATNIINFOUSER: 'dodatniInfoUser/UPDATE_DODATNIINFOUSER',
  DELETE_DODATNIINFOUSER: 'dodatniInfoUser/DELETE_DODATNIINFOUSER',
  RESET: 'dodatniInfoUser/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDodatniInfoUser>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type DodatniInfoUserState = Readonly<typeof initialState>;

// Reducer

export default (state: DodatniInfoUserState = initialState, action): DodatniInfoUserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_DODATNIINFOUSERS):
    case REQUEST(ACTION_TYPES.FETCH_DODATNIINFOUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DODATNIINFOUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_DODATNIINFOUSER):
    case REQUEST(ACTION_TYPES.UPDATE_DODATNIINFOUSER):
    case REQUEST(ACTION_TYPES.DELETE_DODATNIINFOUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_DODATNIINFOUSERS):
    case FAILURE(ACTION_TYPES.FETCH_DODATNIINFOUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DODATNIINFOUSER):
    case FAILURE(ACTION_TYPES.CREATE_DODATNIINFOUSER):
    case FAILURE(ACTION_TYPES.UPDATE_DODATNIINFOUSER):
    case FAILURE(ACTION_TYPES.DELETE_DODATNIINFOUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_DODATNIINFOUSERS):
    case SUCCESS(ACTION_TYPES.FETCH_DODATNIINFOUSER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_DODATNIINFOUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_DODATNIINFOUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_DODATNIINFOUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_DODATNIINFOUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/dodatni-info-users';
const apiSearchUrl = 'api/_search/dodatni-info-users';

// Actions

export const getSearchEntities: ICrudSearchAction<IDodatniInfoUser> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_DODATNIINFOUSERS,
  payload: axios.get<IDodatniInfoUser>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IDodatniInfoUser> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_DODATNIINFOUSER_LIST,
  payload: axios.get<IDodatniInfoUser>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IDodatniInfoUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DODATNIINFOUSER,
    payload: axios.get<IDodatniInfoUser>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IDodatniInfoUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DODATNIINFOUSER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDodatniInfoUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DODATNIINFOUSER,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDodatniInfoUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DODATNIINFOUSER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
