import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPoruka, defaultValue } from 'app/shared/model/poruka.model';

export const ACTION_TYPES = {
  SEARCH_PORUKAS: 'poruka/SEARCH_PORUKAS',
  FETCH_PORUKA_LIST: 'poruka/FETCH_PORUKA_LIST',
  FETCH_PORUKA: 'poruka/FETCH_PORUKA',
  CREATE_PORUKA: 'poruka/CREATE_PORUKA',
  UPDATE_PORUKA: 'poruka/UPDATE_PORUKA',
  DELETE_PORUKA: 'poruka/DELETE_PORUKA',
  RESET: 'poruka/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPoruka>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type PorukaState = Readonly<typeof initialState>;

// Reducer

export default (state: PorukaState = initialState, action): PorukaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_PORUKAS):
    case REQUEST(ACTION_TYPES.FETCH_PORUKA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PORUKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PORUKA):
    case REQUEST(ACTION_TYPES.UPDATE_PORUKA):
    case REQUEST(ACTION_TYPES.DELETE_PORUKA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_PORUKAS):
    case FAILURE(ACTION_TYPES.FETCH_PORUKA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PORUKA):
    case FAILURE(ACTION_TYPES.CREATE_PORUKA):
    case FAILURE(ACTION_TYPES.UPDATE_PORUKA):
    case FAILURE(ACTION_TYPES.DELETE_PORUKA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_PORUKAS):
    case SUCCESS(ACTION_TYPES.FETCH_PORUKA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PORUKA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PORUKA):
    case SUCCESS(ACTION_TYPES.UPDATE_PORUKA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PORUKA):
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

const apiUrl = 'api/porukas';
const apiSearchUrl = 'api/_search/porukas';

// Actions

export const getSearchEntities: ICrudSearchAction<IPoruka> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_PORUKAS,
  payload: axios.get<IPoruka>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IPoruka> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PORUKA_LIST,
  payload: axios.get<IPoruka>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IPoruka> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PORUKA,
    payload: axios.get<IPoruka>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPoruka> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PORUKA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPoruka> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PORUKA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPoruka> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PORUKA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
