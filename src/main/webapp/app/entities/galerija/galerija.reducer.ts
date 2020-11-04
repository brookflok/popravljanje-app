import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGalerija, defaultValue } from 'app/shared/model/galerija.model';

export const ACTION_TYPES = {
  SEARCH_GALERIJAS: 'galerija/SEARCH_GALERIJAS',
  FETCH_GALERIJA_LIST: 'galerija/FETCH_GALERIJA_LIST',
  FETCH_GALERIJA: 'galerija/FETCH_GALERIJA',
  CREATE_GALERIJA: 'galerija/CREATE_GALERIJA',
  UPDATE_GALERIJA: 'galerija/UPDATE_GALERIJA',
  DELETE_GALERIJA: 'galerija/DELETE_GALERIJA',
  RESET: 'galerija/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGalerija>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type GalerijaState = Readonly<typeof initialState>;

// Reducer

export default (state: GalerijaState = initialState, action): GalerijaState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_GALERIJAS):
    case REQUEST(ACTION_TYPES.FETCH_GALERIJA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GALERIJA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GALERIJA):
    case REQUEST(ACTION_TYPES.UPDATE_GALERIJA):
    case REQUEST(ACTION_TYPES.DELETE_GALERIJA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_GALERIJAS):
    case FAILURE(ACTION_TYPES.FETCH_GALERIJA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GALERIJA):
    case FAILURE(ACTION_TYPES.CREATE_GALERIJA):
    case FAILURE(ACTION_TYPES.UPDATE_GALERIJA):
    case FAILURE(ACTION_TYPES.DELETE_GALERIJA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_GALERIJAS):
    case SUCCESS(ACTION_TYPES.FETCH_GALERIJA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GALERIJA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GALERIJA):
    case SUCCESS(ACTION_TYPES.UPDATE_GALERIJA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GALERIJA):
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

const apiUrl = 'api/galerijas';
const apiSearchUrl = 'api/_search/galerijas';

// Actions

export const getSearchEntities: ICrudSearchAction<IGalerija> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_GALERIJAS,
  payload: axios.get<IGalerija>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IGalerija> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GALERIJA_LIST,
  payload: axios.get<IGalerija>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IGalerija> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GALERIJA,
    payload: axios.get<IGalerija>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGalerija> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GALERIJA,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGalerija> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GALERIJA,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGalerija> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GALERIJA,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
