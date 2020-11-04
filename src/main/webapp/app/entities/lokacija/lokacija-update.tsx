import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IKanton } from 'app/shared/model/kanton.model';
import { getEntities as getKantons } from 'app/entities/kanton/kanton.reducer';
import { getEntity, updateEntity, createEntity, reset } from './lokacija.reducer';
import { ILokacija } from 'app/shared/model/lokacija.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ILokacijaUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const LokacijaUpdate = (props: ILokacijaUpdateProps) => {
  const [kantonId, setKantonId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { lokacijaEntity, kantons, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/lokacija');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getKantons();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...lokacijaEntity,
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
          <h2 id="popravljanjeApp.lokacija.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.lokacija.home.createOrEditLabel">Create or edit a Lokacija</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : lokacijaEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="lokacija-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="lokacija-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="adresaLabel" for="lokacija-adresa">
                  <Translate contentKey="popravljanjeApp.lokacija.adresa">Adresa</Translate>
                </Label>
                <AvField id="lokacija-adresa" type="text" name="adresa" />
              </AvGroup>
              <AvGroup>
                <Label id="postanskiBrojLabel" for="lokacija-postanskiBroj">
                  <Translate contentKey="popravljanjeApp.lokacija.postanskiBroj">Postanski Broj</Translate>
                </Label>
                <AvField id="lokacija-postanskiBroj" type="text" name="postanskiBroj" />
              </AvGroup>
              <AvGroup>
                <Label id="gradLabel" for="lokacija-grad">
                  <Translate contentKey="popravljanjeApp.lokacija.grad">Grad</Translate>
                </Label>
                <AvField id="lokacija-grad" type="text" name="grad" />
              </AvGroup>
              <AvGroup>
                <Label for="lokacija-kanton">
                  <Translate contentKey="popravljanjeApp.lokacija.kanton">Kanton</Translate>
                </Label>
                <AvInput id="lokacija-kanton" type="select" className="form-control" name="kanton.id">
                  <option value="" key="0" />
                  {kantons
                    ? kantons.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/lokacija" replace color="info">
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
  kantons: storeState.kanton.entities,
  lokacijaEntity: storeState.lokacija.entity,
  loading: storeState.lokacija.loading,
  updating: storeState.lokacija.updating,
  updateSuccess: storeState.lokacija.updateSuccess,
});

const mapDispatchToProps = {
  getKantons,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(LokacijaUpdate);
