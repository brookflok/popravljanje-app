import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IJavnoPitanje } from 'app/shared/model/javno-pitanje.model';
import { getEntities as getJavnoPitanjes } from 'app/entities/javno-pitanje/javno-pitanje.reducer';
import { IDodatniInfoUser } from 'app/shared/model/dodatni-info-user.model';
import { getEntities as getDodatniInfoUsers } from 'app/entities/dodatni-info-user/dodatni-info-user.reducer';
import { getEntity, updateEntity, createEntity, reset } from './odgovor-na-javno-pitanje.reducer';
import { IOdgovorNaJavnoPitanje } from 'app/shared/model/odgovor-na-javno-pitanje.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IOdgovorNaJavnoPitanjeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const OdgovorNaJavnoPitanjeUpdate = (props: IOdgovorNaJavnoPitanjeUpdateProps) => {
  const [javnoPitanjeId, setJavnoPitanjeId] = useState('0');
  const [dodatniinfoUserId, setDodatniinfoUserId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { odgovorNaJavnoPitanjeEntity, javnoPitanjes, dodatniInfoUsers, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/odgovor-na-javno-pitanje');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getJavnoPitanjes();
    props.getDodatniInfoUsers();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.datum = convertDateTimeToServer(values.datum);

    if (errors.length === 0) {
      const entity = {
        ...odgovorNaJavnoPitanjeEntity,
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
          <h2 id="popravljanjeApp.odgovorNaJavnoPitanje.home.createOrEditLabel">
            <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.home.createOrEditLabel">
              Create or edit a OdgovorNaJavnoPitanje
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : odgovorNaJavnoPitanjeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="odgovor-na-javno-pitanje-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="odgovor-na-javno-pitanje-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="odgovorLabel" for="odgovor-na-javno-pitanje-odgovor">
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.odgovor">Odgovor</Translate>
                </Label>
                <AvField id="odgovor-na-javno-pitanje-odgovor" type="text" name="odgovor" />
              </AvGroup>
              <AvGroup>
                <Label id="datumLabel" for="odgovor-na-javno-pitanje-datum">
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.datum">Datum</Translate>
                </Label>
                <AvInput
                  id="odgovor-na-javno-pitanje-datum"
                  type="datetime-local"
                  className="form-control"
                  name="datum"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.odgovorNaJavnoPitanjeEntity.datum)}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="prikazLabel">
                  <AvInput id="odgovor-na-javno-pitanje-prikaz" type="checkbox" className="form-check-input" name="prikaz" />
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.prikaz">Prikaz</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label for="odgovor-na-javno-pitanje-javnoPitanje">
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.javnoPitanje">Javno Pitanje</Translate>
                </Label>
                <AvInput id="odgovor-na-javno-pitanje-javnoPitanje" type="select" className="form-control" name="javnoPitanje.id">
                  <option value="" key="0" />
                  {javnoPitanjes
                    ? javnoPitanjes.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="odgovor-na-javno-pitanje-dodatniinfoUser">
                  <Translate contentKey="popravljanjeApp.odgovorNaJavnoPitanje.dodatniinfoUser">Dodatniinfo User</Translate>
                </Label>
                <AvInput id="odgovor-na-javno-pitanje-dodatniinfoUser" type="select" className="form-control" name="dodatniinfoUser.id">
                  <option value="" key="0" />
                  {dodatniInfoUsers
                    ? dodatniInfoUsers.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/odgovor-na-javno-pitanje" replace color="info">
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
  javnoPitanjes: storeState.javnoPitanje.entities,
  dodatniInfoUsers: storeState.dodatniInfoUser.entities,
  odgovorNaJavnoPitanjeEntity: storeState.odgovorNaJavnoPitanje.entity,
  loading: storeState.odgovorNaJavnoPitanje.loading,
  updating: storeState.odgovorNaJavnoPitanje.updating,
  updateSuccess: storeState.odgovorNaJavnoPitanje.updateSuccess,
});

const mapDispatchToProps = {
  getJavnoPitanjes,
  getDodatniInfoUsers,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(OdgovorNaJavnoPitanjeUpdate);
