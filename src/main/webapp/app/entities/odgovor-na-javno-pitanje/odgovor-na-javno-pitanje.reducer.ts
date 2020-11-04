import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IOdgovorNaJavnoPitanje, defaultValue } from 'app/shared/model/odgovor-na-javno-pitanje.model';

export const ACTION_TYPES = {
  SEARCH_ODGOVORNAJAVNOPITANJES: 'odgovorNaJavnoPitanje/SEARCH_ODGOVORNAJAVNOPITANJES',
  FETCH_ODGOVORNAJAVNOPITANJE_LIST: 'odgovorNaJavnoPitanje/FETCH_ODGOVORNAJAVNOPITANJE_LIST',
  FETCH_ODGOVORNAJAVNOPITANJE: 'odgovorNaJavnoPitanje/FETCH_ODGOVORNAJAVNOPITANJE',
  CREATE_ODGOVORNAJAVNOPITANJE: 'odgovorNaJavnoPitanje/CREATE_ODGOVORNAJAVNOPITANJE',
  UPDATE_ODGOVORNAJAVNOPITANJE: 'odgovorNaJavnoPitanje/UPDATE_ODGOVORNAJAVNOPITANJE',
  DELETE_ODGOVORNAJAVNOPITANJE: 'odgovorNaJavnoPitanje/DELETE_ODGOVORNAJAVNOPITANJE',
  RESET: 'odgovorNaJavnoPitanje/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IOdgovorNaJavnoPitanje>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type OdgovorNaJavnoPitanjeState = Readonly<typeof initialState>;

// Reducer

export default (state: OdgovorNaJavnoPitanjeState = initialState, action): OdgovorNaJavnoPitanjeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_ODGOVORNAJAVNOPITANJES):
    case REQUEST(ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_ODGOVORNAJAVNOPITANJE):
    case REQUEST(ACTION_TYPES.UPDATE_ODGOVORNAJAVNOPITANJE):
    case REQUEST(ACTION_TYPES.DELETE_ODGOVORNAJAVNOPITANJE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.SEARCH_ODGOVORNAJAVNOPITANJES):
    case FAILURE(ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE):
    case FAILURE(ACTION_TYPES.CREATE_ODGOVORNAJAVNOPITANJE):
    case FAILURE(ACTION_TYPES.UPDATE_ODGOVORNAJAVNOPITANJE):
    case FAILURE(ACTION_TYPES.DELETE_ODGOVORNAJAVNOPITANJE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.SEARCH_ODGOVORNAJAVNOPITANJES):
    case SUCCESS(ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_ODGOVORNAJAVNOPITANJE):
    case SUCCESS(ACTION_TYPES.UPDATE_ODGOVORNAJAVNOPITANJE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_ODGOVORNAJAVNOPITANJE):
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

const apiUrl = 'api/odgovor-na-javno-pitanjes';
const apiSearchUrl = 'api/_search/odgovor-na-javno-pitanjes';

// Actions

export const getSearchEntities: ICrudSearchAction<IOdgovorNaJavnoPitanje> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_ODGOVORNAJAVNOPITANJES,
  payload: axios.get<IOdgovorNaJavnoPitanje>(`${apiSearchUrl}?query=${query}`),
});

export const getEntities: ICrudGetAllAction<IOdgovorNaJavnoPitanje> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE_LIST,
  payload: axios.get<IOdgovorNaJavnoPitanje>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IOdgovorNaJavnoPitanje> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ODGOVORNAJAVNOPITANJE,
    payload: axios.get<IOdgovorNaJavnoPitanje>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IOdgovorNaJavnoPitanje> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ODGOVORNAJAVNOPITANJE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IOdgovorNaJavnoPitanje> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ODGOVORNAJAVNOPITANJE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IOdgovorNaJavnoPitanje> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ODGOVORNAJAVNOPITANJE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
