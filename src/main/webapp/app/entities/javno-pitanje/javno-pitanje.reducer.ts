import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IJavnoPitanje, defaultValue } from 'app/shared/model/javno-pitanje.model';

export const ACTION_TYPES = {
  SEARCH_JAVNOPITANJES: 'javnoPitanje/SEARCH_JAVNOPITANJES',
  FETCH_JAVNOPITANJE_LIST: 'javnoPitanje/FETCH_JAVNOPITANJE_LIST',
  FETCH_JAVNOPITANJE: 'javnoPitanje/FETCH_JAVNOPITANJE',
  CREATE_JAVNOPITANJE: 'javnoPitanje/CREATE_JAVNOPITANJE',
  UPDATE_JAVNOPITANJE: 'javnoPitanje/UPDATE_JAVNOPITANJE',
  DELETE_JAVNOPITANJE: 'javnoPitanje/DELETE_JAVNOPITANJE',
  RESET: 'javnoPitanje/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IJavnoPitanje>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type JavnoPitanjeState = Readonly<typeof initialState>;

// Reducer

export default (state: JavnoPitanjeState = initialState, action): JavnoPitanjeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_JAVNOPITANJES):
    case REQUEST(ACTION_TYPES.FETCH_JAVNOPITANJE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_JAVNOPITANJE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_JAVNOPITANJE):
    case REQUEST(ACTION_TYPES.UPDATE_JAVNOPITANJE):
    case REQUEST(ACTION_TYPES.DELETE_JAVNOPITANJE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_JAVNOPITANJES):
    case FAILURE(ACTION_TYPES.FETCH_JAVNOPITANJE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_JAVNOPITANJE):
    case FAILURE(ACTION_TYPES.CREATE_JAVNOPITANJE):
    case FAILURE(ACTION_TYPES.UPDATE_JAVNOPITANJE):
    case FAILURE(ACTION_TYPES.DELETE_JAVNOPITANJE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_JAVNOPITANJES):
    case SUCCESS(ACTION_TYPES.FETCH_JAVNOPITANJE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_JAVNOPITANJE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_JAVNOPITANJE):
    case SUCCESS(ACTION_TYPES.UPDATE_JAVNOPITANJE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_JAVNOPITANJE):
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

const apiUrl = 'api/javno-pitanjes';
const apiSearchUrl = 'api/_search/javno-pitanjes';

// Actions

export const getSearchEntities: ICrudSearchAction<IJavnoPitanje> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_JAVNOPITANJES,
  payload: axios.get<IJavnoPitanje>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IJavnoPitanje> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_JAVNOPITANJE_LIST,
  payload: axios.get<IJavnoPitanje>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IJavnoPitanje> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_JAVNOPITANJE,
    payload: axios.get<IJavnoPitanje>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IJavnoPitanje> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_JAVNOPITANJE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IJavnoPitanje> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_JAVNOPITANJE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IJavnoPitanje> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_JAVNOPITANJE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
