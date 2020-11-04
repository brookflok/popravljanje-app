import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IUcesnici, defaultValue } from 'app/shared/model/ucesnici.model';

export const ACTION_TYPES = {
  SEARCH_UCESNICIS: 'ucesnici/SEARCH_UCESNICIS',
  FETCH_UCESNICI_LIST: 'ucesnici/FETCH_UCESNICI_LIST',
  FETCH_UCESNICI: 'ucesnici/FETCH_UCESNICI',
  CREATE_UCESNICI: 'ucesnici/CREATE_UCESNICI',
  UPDATE_UCESNICI: 'ucesnici/UPDATE_UCESNICI',
  DELETE_UCESNICI: 'ucesnici/DELETE_UCESNICI',
  RESET: 'ucesnici/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IUcesnici>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type UcesniciState = Readonly<typeof initialState>;

// Reducer

export default (state: UcesniciState = initialState, action): UcesniciState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_UCESNICIS):
    case REQUEST(ACTION_TYPES.FETCH_UCESNICI_LIST):
    case REQUEST(ACTION_TYPES.FETCH_UCESNICI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_UCESNICI):
    case REQUEST(ACTION_TYPES.UPDATE_UCESNICI):
    case REQUEST(ACTION_TYPES.DELETE_UCESNICI):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_UCESNICIS):
    case FAILURE(ACTION_TYPES.FETCH_UCESNICI_LIST):
    case FAILURE(ACTION_TYPES.FETCH_UCESNICI):
    case FAILURE(ACTION_TYPES.CREATE_UCESNICI):
    case FAILURE(ACTION_TYPES.UPDATE_UCESNICI):
    case FAILURE(ACTION_TYPES.DELETE_UCESNICI):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_UCESNICIS):
    case SUCCESS(ACTION_TYPES.FETCH_UCESNICI_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_UCESNICI):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_UCESNICI):
    case SUCCESS(ACTION_TYPES.UPDATE_UCESNICI):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_UCESNICI):
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

const apiUrl = 'api/ucesnicis';
const apiSearchUrl = 'api/_search/ucesnicis';

// Actions

export const getSearchEntities: ICrudSearchAction<IUcesnici> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_UCESNICIS,
  payload: axios.get<IUcesnici>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IUcesnici> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_UCESNICI_LIST,
  payload: axios.get<IUcesnici>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IUcesnici> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_UCESNICI,
    payload: axios.get<IUcesnici>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IUcesnici> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_UCESNICI,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IUcesnici> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_UCESNICI,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IUcesnici> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_UCESNICI,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
