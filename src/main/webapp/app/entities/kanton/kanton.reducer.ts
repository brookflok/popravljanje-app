import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IKanton, defaultValue } from 'app/shared/model/kanton.model';

export const ACTION_TYPES = {
  SEARCH_KANTONS: 'kanton/SEARCH_KANTONS',
  FETCH_KANTON_LIST: 'kanton/FETCH_KANTON_LIST',
  FETCH_KANTON: 'kanton/FETCH_KANTON',
  CREATE_KANTON: 'kanton/CREATE_KANTON',
  UPDATE_KANTON: 'kanton/UPDATE_KANTON',
  DELETE_KANTON: 'kanton/DELETE_KANTON',
  RESET: 'kanton/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IKanton>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type KantonState = Readonly<typeof initialState>;

// Reducer

export default (state: KantonState = initialState, action): KantonState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_KANTONS):
    case REQUEST(ACTION_TYPES.FETCH_KANTON_LIST):
    case REQUEST(ACTION_TYPES.FETCH_KANTON):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_KANTON):
    case REQUEST(ACTION_TYPES.UPDATE_KANTON):
    case REQUEST(ACTION_TYPES.DELETE_KANTON):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_KANTONS):
    case FAILURE(ACTION_TYPES.FETCH_KANTON_LIST):
    case FAILURE(ACTION_TYPES.FETCH_KANTON):
    case FAILURE(ACTION_TYPES.CREATE_KANTON):
    case FAILURE(ACTION_TYPES.UPDATE_KANTON):
    case FAILURE(ACTION_TYPES.DELETE_KANTON):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_KANTONS):
    case SUCCESS(ACTION_TYPES.FETCH_KANTON_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_KANTON):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_KANTON):
    case SUCCESS(ACTION_TYPES.UPDATE_KANTON):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_KANTON):
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

const apiUrl = 'api/kantons';
const apiSearchUrl = 'api/_search/kantons';

// Actions

export const getSearchEntities: ICrudSearchAction<IKanton> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_KANTONS,
  payload: axios.get<IKanton>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IKanton> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_KANTON_LIST,
  payload: axios.get<IKanton>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IKanton> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_KANTON,
    payload: axios.get<IKanton>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IKanton> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_KANTON,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IKanton> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_KANTON,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IKanton> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_KANTON,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
