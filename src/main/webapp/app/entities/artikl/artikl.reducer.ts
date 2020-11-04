import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IArtikl, defaultValue } from 'app/shared/model/artikl.model';

export const ACTION_TYPES = {
  SEARCH_ARTIKLS: 'artikl/SEARCH_ARTIKLS',
  FETCH_ARTIKL_LIST: 'artikl/FETCH_ARTIKL_LIST',
  FETCH_ARTIKL: 'artikl/FETCH_ARTIKL',
  CREATE_ARTIKL: 'artikl/CREATE_ARTIKL',
  UPDATE_ARTIKL: 'artikl/UPDATE_ARTIKL',
  DELETE_ARTIKL: 'artikl/DELETE_ARTIKL',
  RESET: 'artikl/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IArtikl>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ArtiklState = Readonly<typeof initialState>;

// Reducer

export default (state: ArtiklState = initialState, action): ArtiklState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ARTIKLS):
    case REQUEST(ACTION_TYPES.FETCH_ARTIKL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ARTIKL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ARTIKL):
    case REQUEST(ACTION_TYPES.UPDATE_ARTIKL):
    case REQUEST(ACTION_TYPES.DELETE_ARTIKL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ARTIKLS):
    case FAILURE(ACTION_TYPES.FETCH_ARTIKL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ARTIKL):
    case FAILURE(ACTION_TYPES.CREATE_ARTIKL):
    case FAILURE(ACTION_TYPES.UPDATE_ARTIKL):
    case FAILURE(ACTION_TYPES.DELETE_ARTIKL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ARTIKLS):
    case SUCCESS(ACTION_TYPES.FETCH_ARTIKL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ARTIKL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ARTIKL):
    case SUCCESS(ACTION_TYPES.UPDATE_ARTIKL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ARTIKL):
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

const apiUrl = 'api/artikls';
const apiSearchUrl = 'api/_search/artikls';

// Actions

export const getSearchEntities: ICrudSearchAction<IArtikl> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ARTIKLS,
  payload: axios.get<IArtikl>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IArtikl> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ARTIKL_LIST,
  payload: axios.get<IArtikl>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IArtikl> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ARTIKL,
    payload: axios.get<IArtikl>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IArtikl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ARTIKL,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IArtikl> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ARTIKL,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IArtikl> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ARTIKL,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
