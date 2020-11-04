import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IEntiteti } from 'app/shared/model/entiteti.model';
import { getEntities as getEntitetis } from 'app/entities/entiteti/entiteti.reducer';
import { getEntity, updateEntity, createEntity, reset } from './kanton.reducer';
import { IKanton } from 'app/shared/model/kanton.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IKantonUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const KantonUpdate = (props: IKantonUpdateProps) => {
  const [entitetId, setEntitetId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { kantonEntity, entitetis, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/kanton');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getEntitetis();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...kantonEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="popravljanjeApp.kanton.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.kanton.home.createOrEditLabel">Create or edit a Kanton</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : kantonEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="kanton-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="kanton-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="imeKantonaLabel" for="kanton-imeKantona">
                  <Translate contentKey="popravljanjeApp.kanton.imeKantona">Ime Kantona</Translate>
                </Label>
                <AvField id="kanton-imeKantona" type="text" name="imeKantona" />
              </AvGroup>
              <AvGroup>
                <Label for="kanton-entitet">
                  <Translate contentKey="popravljanjeApp.kanton.entitet">Entitet</Translate>
                </Label>
                <AvInput id="kanton-entitet" type="select" className="form-control" name="entitet.id">
                  <option value="" key="0" />
                  {entitetis
                    ? entitetis.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/kanton" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  entitetis: storeState.entiteti.entities,
  kantonEntity: storeState.kanton.entity,
  loading: storeState.kanton.loading,
  updating: storeState.kanton.updating,
  updateSuccess: storeState.kanton.updateSuccess,
});

const mapDispatchToProps = {
  getEntitetis,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(KantonUpdate);
